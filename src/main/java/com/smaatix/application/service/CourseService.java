package com.smaatix.application.service;


import com.smaatix.application.entity.Course;
import com.smaatix.application.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }

  public Course getCourseById(int id) {
    return courseRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
  }

  public String createCourse(Course course) {
    courseRepository.save(course);
    return "Course added successfully!";
  }

  public Course updateCourse(int id, Course courseDetails) {
    return courseRepository.findById(id).map(course -> {
      course.setCoursename(courseDetails.getCoursename());
      course.setCoursetitle(courseDetails.getCoursetitle());
      course.setCoursedescription(courseDetails.getCoursedescription());
      course.setVideo(courseDetails.getVideo());
      course.setCourseimg(courseDetails.getCourseimg());
      return courseRepository.save(course);
    }).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
  }


  public void deleteCourse(int id) {
    if (courseRepository.existsById(id)) {
      courseRepository.deleteById(id);
    } else {
      throw new RuntimeException("Course not found with id: " + id);
    }
  }
}
