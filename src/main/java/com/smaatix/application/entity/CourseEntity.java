package com.smaatix.application.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "courseentity")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

//  @OneToMany(mappedBy = "CourseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  @JsonIgnore
//  private List<HistoryEntity> histories;

  @Column(name = "domain", nullable = false)
  private String domain;

  @Column(name = "course", nullable = false)
  private String course;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;


  private int videoId;

  @Column(name = "videourl", nullable = false)
  private String videourl;

  @Column(name = "imgurl", nullable = false)
  private String imgurl;

  // Default Constructor
  public CourseEntity() {
  }


  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public int getVideoId() {
    return videoId;
  }

  public void setVideoId(int videoId) {
    this.videoId = videoId;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVideourl() {
    return videourl;
  }

  public void setVideourl(String videourl) {
    this.videourl = videourl;
  }

  public String getImgurl() {
    return imgurl;
  }

  public void setImgurl(String imgurl) {
    this.imgurl = imgurl;
  }

//  public List<HistoryEntity> getHistories() {
//    return histories;
//  }
//
//  public void setHistories(List<HistoryEntity> histories) {
//    this.histories = histories;
//  }
//}
}
