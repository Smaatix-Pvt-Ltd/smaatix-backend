package com.smaatix.application.controller;


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
@CrossOrigin(origins = "*")
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

    @PostMapping
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
    public ResponseEntity<?> loginUser(
      @RequestParam String identifier,  // Accepts email or phone
      @RequestParam String password
    ) {
        Optional<UserEntity> user = userService.authenticateUser(identifier, password);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email/phone or password");
        }
    }
}

