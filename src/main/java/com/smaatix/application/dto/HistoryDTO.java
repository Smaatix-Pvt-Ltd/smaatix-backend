package com.smaatix.application.dto;

public class HistoryDTO {
    private int videoId;
    private String videoTitle;
    private String imgUrl;
    private String videoUrl;
    private double pausedAt;
    private double duration;

    public HistoryDTO() {
    }

    public HistoryDTO(int videoId, String videoTitle, String imgUrl, String videoUrl, double pausedAt, double duration) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.pausedAt = pausedAt;
        this.duration = duration;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public double getPausedAt() {
        return pausedAt;
    }

    public void setPausedAt(double pausedAt) {
        this.pausedAt = pausedAt;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
