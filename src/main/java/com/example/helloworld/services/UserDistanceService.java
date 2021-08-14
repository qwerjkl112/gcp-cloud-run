package com.example.helloworld.services;

import java.util.List;

import com.example.helloworld.model.LeaderboardEntry;
import com.example.helloworld.model.User;
import com.example.helloworld.model.UserDistanceEntry;

public interface UserDistanceService {
    User createUser(String username) throws Exception;
    Boolean logDistance(UserDistanceEntry userDistanceEntry) throws Exception;
    List<UserDistanceEntry> getUserHistory(String username) throws Exception;
    List<LeaderboardEntry> getWeeklyScoreBoard() throws Exception;
}
