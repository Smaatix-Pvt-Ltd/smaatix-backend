package com.smaatix.application.controller;

import com.smaatix.application.entity.HistoryEntity;
import com.smaatix.application.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/histories")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // Get all histories
    @GetMapping
    public List<HistoryEntity> getAllHistories() {
        return historyService.getAllHistories();
    }

    // Get a history by ID
    @GetMapping("/{historyId}")
    public Optional<HistoryEntity> getHistoryById(@PathVariable int historyId) {
        return historyService.getHistoryById(historyId);
    }

    // Get histories by user ID
    @GetMapping("/user/{userId}")
    public List<HistoryEntity> getHistoriesByUserId(@PathVariable int userId) {
        return historyService.getHistoriesByUserId(userId);
    }

    // Create a new history for a user
//    @PostMapping("/user/{userId}")
//    public HistoryEntity createHistory(@PathVariable int userId, @RequestBody HistoryEntity historyEntity) {
//        return historyService.createHistory(userId, historyEntity);
//    }
    @PostMapping("/user/{userId}/Course/{Id}")
    public HistoryEntity createHistory(
      @PathVariable int userId,
      @PathVariable long Id,
      @RequestBody HistoryEntity historyEntity) {

        return historyService.createHistory(userId, Id, historyEntity);
    }

    // Update a history
    @PutMapping("/{historyId}")
    public HistoryEntity updateHistory(@PathVariable int historyId, @RequestBody HistoryEntity historyDetails) {
        return historyService.updateHistory(historyId, historyDetails);
    }

    // Delete a history
    @DeleteMapping("/{historyId}")
    public void deleteHistory(@PathVariable int historyId) {
        historyService.deleteHistory(historyId);
    }
}