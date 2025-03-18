package com.smaatix.application.controller;

import com.smaatix.application.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CoursesController {

  @Autowired
  private CourseService courseService;

  // API to fetch all courses name
  @GetMapping("/courses/name")
  public ResponseEntity<List<String>> getAllCourseNames() {
    List<String> courseNames = courseService.getAllCoursename();
    return ResponseEntity.ok(courseNames);
  }
}
