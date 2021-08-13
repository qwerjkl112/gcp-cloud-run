package com.example.helloworld.model;

public class LeaderboardEntry {
    private String username;
    private Float totalDistance;

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(String username, Float totalDistance) {
        this.username = username;
        this.totalDistance = totalDistance;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Float totalDistance) {
        this.totalDistance = totalDistance;
    }
}