package com.smaatix.application.repository;

import com.smaatix.application.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEntityRepository  extends JpaRepository<CourseEntity, Long> {
  List<CourseEntity> findByDomain(String domain);

  List<CourseEntity> findByDomainAndCourse(String domain, String course);

  @Query("SELECT DISTINCT c.domain FROM CourseEntity c")
  List<String> findDistinctDomains();


  @Query("SELECT DISTINCT c.course FROM CourseEntity c WHERE c.domain = :domain")
  List<String> findCoursesByDomain(@Param("domain") String domain);

  Optional<CourseEntity> findByTitle(String title);

  boolean existsByTitle(String title);
}