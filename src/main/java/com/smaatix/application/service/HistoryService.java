package com.smaatix.application.service;

import com.smaatix.application.dto.HistoryDTO;
import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.entity.HistoryEntity;
import com.smaatix.application.entity.UserEntity;
import com.smaatix.application.repository.CourseEntityRepository;
import com.smaatix.application.repository.HistoryRepository;
import com.smaatix.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    // Get histories by user ID
//    public List<HistoryEntity> getHistoriesByUserId(int userId) {
//        return historyRepository.findByUserEntity_UserId(userId);
//    }

//    // Create a new history for a user

//
//    public HistoryEntity createHistory(int userId, long Id, HistoryEntity historyEntity) {
//        UserEntity userEntity = userRepository.findById(userId)
//          .orElseThrow(() -> new RuntimeException("User not found"));
//
//        CourseEntity courseEntity = courseEntityRepository.findById(Id)
//          .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        historyEntity.setUserEntity(userEntity);
////        historyEntity.setCourseEntity(courseEntity);
//        historyEntity.setCreatedAt(LocalDateTime.now());
//
//        return historyRepository.save(historyEntity);
//    }

@Transactional
public HistoryEntity createOrUpdateHistory(int userId, long videoId, HistoryEntity historyEntity) {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new RuntimeException("User not found"));

    CourseEntity courseEntity = courseEntityRepository.findById(videoId) // Rename if using VideoEntity
      .orElseThrow(() -> new RuntimeException("Course not found"));

    // Check if history already exists for the user and course
    Optional<HistoryEntity> existingHistory = historyRepository.findByUserEntityAndVideoId(userEntity, (int) videoId);
    if (existingHistory.isPresent()) {
        HistoryEntity historyToUpdate = existingHistory.get();
        historyToUpdate.setPausedAt(historyEntity.getPausedAt()); // Update required fields
        return historyRepository.save(historyToUpdate);
    } else {
        // Create new history entry if not found
        historyEntity.setUserEntity(userEntity);
        historyEntity.setVideoId((int) videoId);
        historyEntity.setCreatedAt(LocalDateTime.now());
        return historyRepository.save(historyEntity);
    }
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


    public List<HistoryDTO> getHistoryByUserId(int userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<HistoryEntity> historyList = historyRepository.findByUserEntityOrderByCreatedAtDesc(user.get());

            return historyList.stream().map(history -> {
                Optional<CourseEntity> courseOpt = courseEntityRepository.findById((long) history.getVideoId());
                if (courseOpt.isPresent()) {
                    CourseEntity course = courseOpt.get();
                    return new HistoryDTO(
                      history.getVideoId(),
                      course.getTitle(),
                      course.getImgurl(),
                      course.getVideourl(),
                      history.getPausedAt(),
                      history.getDuration()
                    );
                } else {
                    return null;
                }
            }).filter(dto -> dto != null).collect(Collectors.toList());
        }
        return List.of(); // Return empty list if user not found
    }



}