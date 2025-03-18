package com.smaatix.application.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

  @Service
  public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadFile(MultipartFile file) throws IOException {
      String url = "http://192.168.1.146:3000/api/upload"; // Express API URL

      // Save file locally (optional)
      String localPath = "C:/uploads/";
      File directory = new File(localPath);
      if (!directory.exists()) {
        directory.mkdirs();
      }

      File savedFile = new File(localPath + file.getOriginalFilename());
      try (FileOutputStream fos = new FileOutputStream(savedFile)) {
        fos.write(file.getBytes());
      }

      // Prepare headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      // Convert MultipartFile to MultiValueMap
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", new FileSystemResource(savedFile)); // Match Express API field name

      // Create request entity
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // Send the request and get the response
      ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

      // Parse the response JSON to extract the filename
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      String filename = jsonNode.get("fileDetails").get("filename").asText();

      // Construct and return the file URL
      return "http://192.168.1.146:3000/api/files/" + filename;
    }
  }

