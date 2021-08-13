package com.example.helloworld.model;

public class UserDistanceEntry {
    private Long userId;
    private String username;
    private Float distance;
    private String timestamp;

    public UserDistanceEntry() {}

    public UserDistanceEntry(Long userId, String username, Float distance, String timestamp) {
        this.userId = userId;
        this.username = username;
        this.distance = distance;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "[UserId: " + userId + " username: " + username + " distance: " + distance + " timestamp: " + timestamp + " ]";

    }
}