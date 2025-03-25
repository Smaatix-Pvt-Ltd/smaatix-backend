package com.smaatix.application.repository;

import com.smaatix.application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByUserEmailOrUserPhoneAndUserPassword(String email, String phone, String password);
  }
