package com.smaatix.application.service;

import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.entity.HistoryEntity;
import com.smaatix.application.entity.UserEntity;
import com.smaatix.application.repository.CourseEntityRepository;
import com.smaatix.application.repository.HistoryRepository;
import com.smaatix.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseEntityRepository courseEntityRepository;

    // Get all histories
    public List<HistoryEntity> getAllHistories() {
        return historyRepository.findAll();
    }

    // Get a history by ID
    public Optional<HistoryEntity> getHistoryById(int historyId) {
        return historyRepository.findById(historyId);
    }

    // Get histories by user ID
    public List<HistoryEntity> getHistoriesByUserId(int userId) {
        return historyRepository.findByUserEntity_UserId(userId);
    }

//    // Create a new history for a user
//    public HistoryEntity createHistory(int userId, HistoryEntity historyEntity) {
//        UserEntity userEntity = userRepository.findById(userId)
//          .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//        historyEntity.setUserEntity(userEntity); // Set the user
//        historyEntity.setCreatedAt(LocalDateTime.now()); // Set createdAt timestamp
//        return historyRepository.save(historyEntity);
//    }

    public HistoryEntity createHistory(int userId, long Id, HistoryEntity historyEntity) {
        UserEntity userEntity = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("User not found"));

        CourseEntity courseEntity = courseEntityRepository.findById(Id)
          .orElseThrow(() -> new RuntimeException("Course not found"));

        historyEntity.setUserEntity(userEntity);
//        historyEntity.setCourseEntity(courseEntity);
        historyEntity.setCreatedAt(LocalDateTime.now());

        return historyRepository.save(historyEntity);
    }



    // Update a history
    public HistoryEntity updateHistory(int historyId, HistoryEntity historyDetails) {
        HistoryEntity history = historyRepository.findById(historyId)
          .orElseThrow(() -> new RuntimeException("History not found with id: " + historyId));

        history.setVideoId(historyDetails.getVideoId());
        history.setDuration(historyDetails.getDuration());
        history.setPausedAt(historyDetails.getPausedAt());

        return historyRepository.save(history);
    }

    // Delete a history
    public void deleteHistory(int historyId) {
        historyRepository.deleteById(historyId);
    }
}