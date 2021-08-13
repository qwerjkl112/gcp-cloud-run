package com.example.helloworld.services.impl;

import com.example.helloworld.services.UserDistanceService;

import java.util.List;
import java.util.stream.Collectors;

import com.example.helloworld.dao.UserDistanceDao;
import com.example.helloworld.model.LeaderboardEntry;
import com.example.helloworld.model.User;
import com.example.helloworld.model.UserDistanceEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDistanceServiceImpl implements UserDistanceService {
    
    @Autowired
    private UserDistanceDao userDistanceDao;

    @Override
    public User createUser(String username) throws Exception {
        return userDistanceDao.insertUser(username);
    }

    @Override
    public Boolean logDistance(UserDistanceEntry entry) throws Exception {
        User user = userDistanceDao.retrieveUser(entry.getUsername());
        return userDistanceDao.insertDistance(user.getUserId(), entry.getDistance());
    }

    @Override
    public List<String> getUserHistory(String username) throws Exception {
        User user = userDistanceDao.retrieveUser(username);
        return userDistanceDao.getUserDistanceEntries(user).stream()
            .map(UserDistanceEntry::toString)
            .collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardEntry> getWeeklyScoreBoard() throws Exception {
        return userDistanceDao.getWeeklyScoreBoard();
    }
}
