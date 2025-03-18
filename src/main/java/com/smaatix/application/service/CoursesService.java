package com.smaatix.application.service;

import com.smaatix.application.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

  @Service
  public class CoursesService {

    @Autowired
    private CourseRepository courseRepository;

    public List<String> getAllCourseNames() {
      return courseRepository.findAllCoursename();
    }
  }
