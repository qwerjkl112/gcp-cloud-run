package com.example.helloworld.services.impl;

import com.example.helloworld.services.UserDistanceService;

import java.util.List;

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
    public User createUser(final String username) throws Exception {
        return userDistanceDao.insertUser(username);
    }

    @Override
    public Boolean logDistance(final UserDistanceEntry entry) throws Exception {
        return userDistanceDao.insertDistance(entry.getUsername(), entry.getDistance());
    }

    @Override
    public List<UserDistanceEntry> getUserHistory(final String username) throws Exception {
        final User user = userDistanceDao.retrieveUser(username);
        return userDistanceDao.getUserDistanceEntries(user);
    }

    @Override
    public List<LeaderboardEntry> getWeeklyScoreBoard() throws Exception {
        return userDistanceDao.getWeeklyScoreBoard();
    }
}
