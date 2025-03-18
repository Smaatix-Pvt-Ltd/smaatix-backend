package com.smaatix.application.entity;

import jakarta.persistence.*;

@Entity
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int courseid;

  @Column(nullable = false)
  private String coursename;
  @Column(nullable = false)
  private String coursetitle;

  @Column(length = 1000)  // Allow longer descriptions
  private String coursedescription;
  private String video;
  private String courseimg;

  public int getCourseid() {
    return courseid;
  }

  public void setCourseid(int courseid) {
    this.courseid = courseid;
  }

  public String getCoursename() {
    return coursename;
  }

  public void setCoursename(String coursename) {
    this.coursename = coursename;
  }

  public String getCoursetitle() {
    return coursetitle;
  }

  public void setCoursetitle(String coursetitle) {
    this.coursetitle = coursetitle;
  }

  public String getCoursedescription() {
    return coursedescription;
  }

  public void setCoursedescription(String coursedescription) {
    this.coursedescription = coursedescription;
  }

  public String getVideo() {
    return video;
  }

  public void setVideo(String video) {
    this.video = video;
  }

  public String getCourseimg() {
    return courseimg;
  }

  public void setCourseimg(String courseimg) {
    this.courseimg = courseimg;
  }
}
