package com.smaatix.application.controller;

import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@Validated
@CrossOrigin(origins = "*")
public class CourseController {

  @Autowired
  private CourseService courseService;

  // ✅ Create a new course (multipart form data)
  @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> addCourse(
    @RequestPart("imgurl") MultipartFile imgFile,
    @RequestPart("videourl") MultipartFile videoFile,
    @RequestParam("videoId") int videoId,
    @RequestParam("course") String course,
    @RequestParam("title") String title,
    @RequestParam("description") String description,
    @RequestParam("domain") String domain
  ) {
    try {
      // Validate files
      if (imgFile.isEmpty() || videoFile.isEmpty()) {
        return ResponseEntity.badRequest().body("Image or video file is empty.");
      }

      // Validate file types (optional)
      if (!imgFile.getContentType().startsWith("image/") || !videoFile.getContentType().startsWith("video/")) {
        return ResponseEntity.badRequest().body("Invalid file type. Image must be an image file, and video must be a video file.");
      }

      // Delegate to Service Layer
      String response = courseService.createCourseEntity(imgFile, videoFile, videoId, course, title, description, domain);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }  catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("File upload failed: " + e.getMessage());
    }
  }

  // ✅ Get all courses
  @GetMapping("/")
  public ResponseEntity<List<CourseEntity>> getAllCourses() {
    List<CourseEntity> courses = courseService.getAllCourses();
    return ResponseEntity.ok(courses);
  }

  // ✅ Get course by ID
  @GetMapping("/{id}")
  public ResponseEntity<CourseEntity> getCourseById(@PathVariable long id) {
    Optional<CourseEntity> course = courseService.getCourseById(id);
    return course.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ✅ Update a course
  @PutMapping("/{id}")
  public ResponseEntity<String> updateCourse(
    @PathVariable long id,
    @RequestBody CourseEntity updatedCourse
  ) {
    try {
      courseService.updateCourseEntity(id, updatedCourse);
      return ResponseEntity.ok("Course updated successfully!");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
  }

  // ✅ Delete a course
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCourse(@PathVariable long id) {
    try {
      courseService.deleteCourse(id);
      return ResponseEntity.ok("Course deleted successfully.");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
    }
  }
}