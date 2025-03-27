package com.smaatix.application.service;

import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.repository.CourseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

  private final CourseEntityRepository courseEntityRepository;
  private final ApiService apiService;

  @Autowired
  public CourseService(CourseEntityRepository courseEntityRepository, ApiService apiService) {
    this.courseEntityRepository = courseEntityRepository;
    this.apiService = apiService;
  }

  /**
   * Creates a new course entity with associated files stored as URLs
   */
  public String createCourseEntity(
    MultipartFile imgFile,
    MultipartFile videoFile,
    int videoId,
    String course,
    String title,
    String description,
    String domain) throws IOException {

    // Create CourseEntity instance
    CourseEntity courseEntity = new CourseEntity();
    courseEntity.setVideoId(videoId);
    courseEntity.setCourse(course);
    courseEntity.setTitle(title);
    courseEntity.setDescription(description);
    courseEntity.setDomain(domain);

    // Upload files and store URLs
    if (!imgFile.isEmpty()) {
      courseEntity.setImgurl(apiService.uploadFile(imgFile));

    }

    if (!videoFile.isEmpty()) {
      courseEntity.setVideourl(apiService.uploadFile(videoFile));

    }

    // Save the entity
    courseEntityRepository.save(courseEntity);

    return "Course created successfully";
  }

  /**
   * Retrieves all courses
   */
  public List<CourseEntity> getAllCourses() {
    return courseEntityRepository.findAll();
  }

  /**
   * Retrieves a course by ID
   */
  public Optional<CourseEntity> getCourseById(Long id) {
    return courseEntityRepository.findById(id);
  }

  /**
   * Updates an existing course
   */
  public void updateCourseEntity(Long id, CourseEntity updatedCourse) {
    CourseEntity existingCourse = courseEntityRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Course not found"));

    existingCourse.setVideoId(updatedCourse.getVideoId());
    existingCourse.setCourse(updatedCourse.getCourse());
    existingCourse.setTitle(updatedCourse.getTitle());
    existingCourse.setDescription(updatedCourse.getDescription());
    existingCourse.setDomain(updatedCourse.getDomain());

    courseEntityRepository.save(existingCourse);
  }

  /**
   * Deletes a course by ID
   */
  public void deleteCourse(Long id) {
    courseEntityRepository.deleteById(id);
  }
}