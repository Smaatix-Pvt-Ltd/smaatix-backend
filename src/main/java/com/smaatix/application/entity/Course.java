//package com.smaatix.application.entity;
//
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "course")
//public class Course {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @Column(name = "domain", nullable = false)
//  private String domain;
//
//  @Column(name = "course", nullable = false)
//  private String course;
//
//  @Column(name = "title", nullable = false)
//  private String title;
//
//  @Column(name = "description", nullable = false)
//  private String description;
//
//  private int videoId;
//
//  @Column(name = "videourl", nullable = false)
//  private String videourl;
//
//  @Column(name = "imgurl", nullable = false)
//  private String imgurl;
//
//  // Default Constructor
//  public Course() {
//  }
//
//
//
//  // Getters and Setters
//  public Long getId() {
//    return id;
//  }
//
//  public void setId(Long id) {
//    this.id = id;
//  }
//
//  public int getVideoId() {
//    return videoId;
//  }
//
//  public void setVideoId(int videoId) {
//    this.videoId = videoId;
//  }
//
//  public String getDomain() {
//    return domain;
//  }
//
//  public void setDomain(String domain) {
//    this.domain = domain;
//  }
//
//  public String getCourse() {
//    return course;
//  }
//
//  public void setCourse(String course) {
//    this.course = course;
//  }
//
//  public String getTitle() {
//    return title;
//  }
//
//  public void setTitle(String title) {
//    this.title = title;
//  }
//
//  public String getDescription() {
//    return description;
//  }
//
//  public void setDescription(String description) {
//    this.description = description;
//  }
//
//  public String getVideourl() {
//    return videourl;
//  }
//
//  public void setVideourl(String videourl) {
//    this.videourl = videourl;
//  }
//
//  public String getImgurl() {
//    return imgurl;
//  }
//
//  public void setImgurl(String imgurl) {
//    this.imgurl = imgurl;
//  }
//}