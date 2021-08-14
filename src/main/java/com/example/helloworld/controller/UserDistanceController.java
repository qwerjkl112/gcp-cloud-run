package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.helloworld.model.LeaderboardEntry;
import com.example.helloworld.model.User;
import com.example.helloworld.model.UserDistanceEntry;
import com.example.helloworld.services.UserDistanceService;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
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
    public ModelAndView createUser(@RequestParam Map<String, String> body) {
        System.out.println("Received request " + body);
        UserDistanceEntry userDistanceEntry = new UserDistanceEntry();
        userDistanceEntry.setUsername(body.get("username"));
        userDistanceEntry.setDistance(Float.parseFloat(body.get("distance")));
        try {
            userDistanceService.logDistance(userDistanceEntry);
            return getHistorybyUsername(body);
        } catch (Exception e) {
            System.out.println("caught exception " + e);
        }
        return null;
    }

    @PostMapping("/getHistoryByUsername")
    public ModelAndView getHistorybyUsername(@RequestParam Map<String, String> body) {
        String username = body.get("username");
        System.out.println("got request " + username);
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("history", userDistanceService.getUserHistory(username));
            return new ModelAndView("userHistory", params);
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

    @GetMapping("/")
    public ModelAndView getScoreboard() {
        Map<String, Object> params = new HashMap<>();
        try {
            List<LeaderboardEntry> rankings = userDistanceService.getWeeklyScoreBoard();
            params.put("rankings", rankings);
            return new ModelAndView("board", params);
        } catch (Exception e) {
            System.out.println("caught exception " + e);
            return null;
        }
    }
}