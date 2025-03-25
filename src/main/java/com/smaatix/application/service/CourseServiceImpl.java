package com.smaatix.application.service;


import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.repository.CourseEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl {

    @Autowired
    private CourseEntityRepository courseEntityRepository;


    public List<CourseEntity> getAllCourses() {
        return courseEntityRepository.findAll();
    }

    public CourseEntity getCourseById(Long id) {
        Optional<CourseEntity> courseEntity = courseEntityRepository.findById(id);
        return courseEntity.orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public List<CourseEntity> createCourse(String domain) {
        return courseEntityRepository.findByDomain(domain);
    }

    public List<CourseEntity> createCourseAndDomain(String domain, String course) {
        return courseEntityRepository.findByDomainAndCourse(domain,course);

    }

    public List<String> getAllDomains() {
        return courseEntityRepository.findAll().stream()
          .map(CourseEntity::getDomain)
          .distinct()
          .collect(Collectors.toList());
    }

    public List<String> getCoursesByDomain(String domain) {
        return courseEntityRepository.findCoursesByDomain(domain);
    }
}