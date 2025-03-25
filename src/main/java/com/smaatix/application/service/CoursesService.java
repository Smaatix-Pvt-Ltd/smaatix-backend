//package com.smaatix.application.service;
//
//import com.smaatix.application.entity.Course;
//import com.smaatix.application.repository.CourseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class CoursesService {
//
//  @Autowired
//  private CourseRepository courseRepository;
//
//  // ✅ Get all courses structured by mainCategory -> subCategory
//  public  List<Course> getCoursesByMainCategory(String mainCategory) {
//    List<Course> courses = courseRepository.findByMainCategory(mainCategory);
//
//    return courses;
//
//
//  }
//}
