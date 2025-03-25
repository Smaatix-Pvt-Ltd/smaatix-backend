package com.smaatix.application.repository;

import com.smaatix.application.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
  List<HistoryEntity> findByUserEntity_UserId(int userId); // Correct method name
}