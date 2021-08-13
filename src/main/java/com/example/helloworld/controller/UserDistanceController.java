package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import com.example.helloworld.model.LeaderboardEntry;
import com.example.helloworld.model.User;
import com.example.helloworld.model.UserDistanceEntry;
import com.example.helloworld.services.UserDistanceService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/distance")
public class UserDistanceController {

    @Autowired
    private UserDistanceService userDistanceService;

    @PostMapping("/createUser/{username}")
    public User createUser(@PathVariable String username) {
        try {
            return userDistanceService.createUser(username);
        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }

    @PostMapping("/createDistance")
    public Boolean createUser(@RequestBody UserDistanceEntry userDistanceEntry) {
        try {
            return userDistanceService.logDistance(userDistanceEntry);
        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }

    @GetMapping("/getHistoryByUsername/{username}")
    public List<String> getHistorybyUsername(@PathVariable String username) {
        try {
            return userDistanceService.getUserHistory(username);
        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }

        @GetMapping("/getWeeklyLeaderboard")
    public List<LeaderboardEntry> getWeeklyLeaderboard() {
        try {
            return userDistanceService.getWeeklyScoreBoard();
        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }
}