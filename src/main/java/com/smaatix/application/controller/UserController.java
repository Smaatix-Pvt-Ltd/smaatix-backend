package com.smaatix.application.controller;


import com.smaatix.application.entity.LoginRequest;
import com.smaatix.application.entity.UserEntity;
import com.smaatix.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Validated
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<UserEntity> getUserById(@PathVariable int userId) {

        return userService.getUserById(userId);
    }

    @PostMapping("/signup")
    public UserEntity createUser(@RequestBody UserEntity userEntity) {

        return userService.createUser(userEntity);
    }

    @PutMapping("/{userId}")
    public UserEntity updateUser(@PathVariable int userId, @RequestBody UserEntity userDetails) {
        return userService.updateUser(userId, userDetails);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {

        userService.deleteUser(userId);
    }

    // ✅ Login API (Email or Phone + Password)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<UserEntity> user = userService.validateUser(loginRequest.getIdentifier(), loginRequest.getPassword());

        return user.map(u -> ResponseEntity.ok("Login successful"))
          .orElse(ResponseEntity.status(401).body("Invalid credentials"));
}
}

