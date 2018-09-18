package com.example.concurrent.user.runner;

public class RequestRunner implements Runner {
    private String stats;
    private Integer userID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void run() {
        for (int i = 1; i <= numberOfRounds; i++) {
            Thread thread = new Thread(new RoundRunner(stats + " Round Number:" + i, userID));
            pool.execute(thread);
        }
    }

    public RequestRunner(String stats, Integer userID) {
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

}