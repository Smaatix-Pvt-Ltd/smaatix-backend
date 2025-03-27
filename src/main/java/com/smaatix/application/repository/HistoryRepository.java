package com.smaatix.application.repository;

import com.smaatix.application.entity.CourseEntity;
import com.smaatix.application.entity.HistoryEntity;
import com.smaatix.application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
//  List<HistoryEntity> findByUserEntity_UserId(int userId); // Correct method name

  Optional<HistoryEntity> findByUserEntityAndVideoId(UserEntity userEntity, int videoId);

  List<HistoryEntity> findByUserEntity_UserId(int userId);


  List<HistoryEntity> findByUserEntityOrderByCreatedAtDesc(UserEntity userEntity);

}