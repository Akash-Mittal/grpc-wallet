package com.example.concurrent.user.runner;

public class UserRunner implements Runner {
    private String stats;
    private Integer userID;

    public void run() {
        for (int i = 1; i <= numberOfRequests; i++) {
            pool.execute(new RequestRunner("User:" + userID + " Request Number:" + i, userID));
        }
    }

    public UserRunner(String stats, Integer userID) {
        super();
        this.stats = stats;
        this.userID = userID;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

}