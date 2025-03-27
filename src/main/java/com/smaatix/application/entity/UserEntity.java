package com.smaatix.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    	@Column(unique = true)

    private String userPassword;
    @Column(unique = true)
    @Email(message = "Email should be valid")
    private String userEmail;
    @Column(unique = true)
    private String userPhone;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HistoryEntity> histories;


    public UserEntity() {
    }

    public UserEntity(int userId, String username, String userPassword, String userEmail, String userPhone) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public List<HistoryEntity> getHistories() {
        return histories;
    }

    public void setHistories(List<HistoryEntity> histories) {
        this.histories = histories;
    }
}