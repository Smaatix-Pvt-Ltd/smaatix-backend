package com.smaatix.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Entity
public class HistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int HistoryId;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP) // Store both date and time
  @Column(name = "created_at", nullable = false, updatable = false) // Cannot be updated
  private LocalDateTime createdAt;

  private int videoId;

  @Column(name = "duration")
  private double duration; // Represents a time duration (e.g., PT2H30M for 2 hours and 30 minutes)

  @Column(name = "paused_at")

  private Double pausedAt;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  @JsonIgnore// Foreign key to User
  private UserEntity userEntity;

  public HistoryEntity() {
  }

  public HistoryEntity(int historyId, LocalDateTime createdAt, int videoId, double duration, Double pausedAt,  UserEntity userEntity) {
    HistoryId = historyId;
    this.createdAt = createdAt;
    this.videoId = videoId;
    this.duration = duration;
    this.pausedAt = pausedAt;
    this.userEntity = userEntity;
  }

  public int getHistoryId() {
    return HistoryId;
  }

  public void setHistoryId(int historyId) {
    HistoryId = historyId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }


  public int getVideoId() {
    return videoId;
  }

  public void setVideoId(int videoId) {
    this.videoId = videoId;
  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }

  public Double getPausedAt() {
    return pausedAt;
  }

  public void setPausedAt(Double pausedAt) {
    this.pausedAt = pausedAt;
  }

  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }
}