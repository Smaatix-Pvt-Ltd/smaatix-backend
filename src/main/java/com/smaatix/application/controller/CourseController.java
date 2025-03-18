package com.smaatix.application.controller;

import com.smaatix.application.entity.Course;
import com.smaatix.application.service.ApiService;
import com.smaatix.application.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "*")
public class CourseController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private ApiService apiService;

  @PostMapping(value = "/courses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> addCourse(
    @RequestPart("courseimg") MultipartFile file,
    @RequestParam("coursename") String coursename,
    @RequestParam("coursetitle") String coursetitle,
    @RequestParam("coursedescription") String coursedescription,
    @RequestParam("video") MultipartFile video
  ) {
    try {
      if (file.isEmpty() || video.isEmpty()) {
        return ResponseEntity.badRequest().body("Image or video file is empty.");
      }

      // Upload image file
      String imageUrl = apiService.uploadFile(file);

      // Upload video file
      String videoUrl = apiService.uploadFile(video);

      // Create and save course
      Course course = new Course();
      course.setCoursename(coursename);
      course.setCoursetitle(coursetitle);
      course.setCoursedescription(coursedescription);
      course.setCourseimg(imageUrl);
      course.setVideo(videoUrl);

      String message = courseService.createCourse(course);
      return ResponseEntity.status(HttpStatus.CREATED).body(message);
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
        .body("Duplicate Entry: Course with the same title or name already exists.");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("File upload failed: " + e.getMessage());
    }
  }

    @GetMapping("/get")
  public ResponseEntity<List<Course>> getAllCourses() {
    List<Course> courses = courseService.getAllCourses();
    return ResponseEntity.ok(courses);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable int id) {
    Course course = courseService.getCourseById(id);
    return ResponseEntity.ok(course);
  }



  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteCourse(@PathVariable int id) {
    courseService.deleteCourse(id);
    return ResponseEntity.ok("Course deleted successfully");
  }
}
