package com.smaatix.application.service;


import com.smaatix.application.entity.UserEntity;
import com.smaatix.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity updateUser(int userId, UserEntity userDetails) {
        return userRepository.findById(userId).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setUserPassword(userDetails.getUserPassword());
            user.setUserEmail(userDetails.getUserEmail());
            user.setUserPhone(userDetails.getUserPhone());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    // ✅ Authenticate user by Email OR Phone + Password
    public Optional<UserEntity> authenticateUser(String identifier, String password) {
        return userRepository.findByUserEmailOrUserPhoneAndUserPassword(identifier, identifier, password);
    }
}
