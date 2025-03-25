//package com.smaatix.application.controller;
//
//
//
//
//import com.smaatix.application.entity.Course;
//import com.smaatix.application.service.CoursesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//public class CoursesController {
//
//  @Autowired
//  private CoursesService coursesService;
//
//  // ✅ Get courses by Main Category
////  @GetMapping("/courses/main/{mainCategory}")
////  public ResponseEntity<Map<String, List<CourseDTO>>> getCoursesByMainCategory(@PathVariable String mainCategory) {
////    Map<String, List<CourseDTO>> courses = coursesService.getCoursesByMainCategory(mainCategory);
////    return ResponseEntity.ok(courses);
////  }
//
//  @GetMapping("/courses/main/{mainCategory}")
//  public ResponseEntity<List<Course>> getCoursesByMainCategory(@PathVariable String mainCategory) {
//   List<Course> courses = coursesService.getCoursesByMainCategory(mainCategory);
//    return ResponseEntity.ok(courses);
//  }
//
//
//
//}
//
