package com.smaatix.application.controller;

import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.service.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/coursesentity")
@CrossOrigin
  public class CourseEntityController {

    @Autowired
    private CourseServiceImpl courseService;
//
//    // Get all courses
//    @GetMapping
//    public List<CourseEntity> getAllCourses() {
//      return courseService.getAllCourses();
//    }
//
//    // Get a course by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<CourseEntity> getCourseById(@PathVariable Long id) {
//      CourseEntity course = courseService.getCourseById(id);
//      return ResponseEntity.ok(course);
//    }




    @GetMapping("/all/{domain}")
   public ResponseEntity<Map<String, List<CourseEntity>>> getCoursesByMainCategory(@PathVariable String domain) {
    List<CourseEntity> courses = courseService.createCourse(domain);
      Map<String, List<CourseEntity>> map=new HashMap<>() ;
      map.put(domain,courses);
    return ResponseEntity.ok(map);
  }


  @GetMapping("/{domain}")
  public ResponseEntity<Map<String, List<String>>> getCourses(@PathVariable String domain) {
    List<String> courses = courseService.getCoursesByDomain(domain);
    Map<String, List<String>> map=new HashMap<>() ;
    map.put(domain,courses);
    return ResponseEntity.ok(map);
  }

  @GetMapping("/{domain}/{course}")
  public ResponseEntity<Map<String, Map<String, List<CourseEntity>>>> getCoursesByMainCategory(@PathVariable String domain,@PathVariable String course) {
    List<CourseEntity> courses = courseService.createCourseAndDomain(domain,course);
    Map<String, Map<String, List<CourseEntity>>> map=new HashMap<>() ;
    Map<String, List<CourseEntity>> mapto = new HashMap<>();
    mapto.put(course,courses);
    map.put(domain,mapto);
    return ResponseEntity.ok(map);
  }

  @GetMapping("/domains")
  public ResponseEntity<List<String>> getAllDomains() {
    List<String> domains = courseService.getAllDomains();
    return ResponseEntity.ok(domains);
  }



//
//    // Update an existing course
//    @PutMapping("/{id}")
//    public ResponseEntity<CourseEntity> updateCourse(@PathVariable Long id, @RequestBody CourseEntity courseDetails) {
//      CourseEntity updatedCourse = courseService.updateCourse(id, courseDetails);
//      return ResponseEntity.ok(updatedCourse);
//    }
//
//    // Delete a course
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
//      courseService.deleteCourse(id);
//      return ResponseEntity.noContent().build();
//    }
  }