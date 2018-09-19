package com.betpawa.wallet.client.enums.runner;

public class RequestRunner implements Runner {
    private String stats;
    private Integer userID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public void run() {
        for (int i = 1; i <= numberOfRounds; i++) {
            pool.execute(new RoundRunner(stats + ":Request Number:" + i, userID));
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