package com.smaatix.application.repository;

import com.smaatix.application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

  @Query("SELECT c.coursename FROM Course c")
  List<String> findAllCoursename();
}
