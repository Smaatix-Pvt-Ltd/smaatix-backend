package com.smaatix.application.controller;

import com.smaatix.application.dto.HistoryDTO;
import com.smaatix.application.entity.HistoryEntity;
import com.smaatix.application.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Validated
@CrossOrigin("http://192.168.1.202:5173")
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
//    // Get histories by user ID
//    @GetMapping("/user/{userId}")
//    public List<HistoryEntity> getHistoriesByUserId(@PathVariable int userId) {
//        return historyService.getHistoriesByUserId(userId);
//    }




    @PostMapping("/user/{userId}/video/{videoId}")
    public HistoryEntity createOrUpdateHistory(
      @PathVariable int userId,
      @PathVariable int videoId,
      @RequestBody HistoryEntity historyEntity) {

        return historyService.createOrUpdateHistory(userId, videoId, historyEntity);
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

    @GetMapping("/user/{userId}")
    public List<HistoryDTO> getHistoryByUserId(@PathVariable int userId) {
        return historyService.getHistoryByUserId(userId);
    }
}