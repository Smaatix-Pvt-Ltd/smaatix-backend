package com.smaatix.application.repository;

import com.smaatix.application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
//  @Query("SELECT u FROM UserEntity u WHERE (u.userEmail = :email OR u.userPhone = :phone) AND u.userPassword = :password")
//  Optional<UserEntity> findByUserEmailOrUserPhoneAndUserPassword(

  //    @Param("email") String email,
//    @Param("phone") String phone,
//    @Param("password") String password
//  );  }
  Optional<UserEntity> findByUserEmailAndUserPassword(String userEmail, String userPassword);

  Optional<UserEntity> findByUserPhoneAndUserPassword(String userPhone, String userPassword);

  Optional<UserEntity> findByUsernameAndUserPassword(String username, String userPassword);
}