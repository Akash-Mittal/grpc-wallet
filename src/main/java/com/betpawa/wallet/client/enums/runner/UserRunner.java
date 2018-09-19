package com.betpawa.wallet.client.enums.runner;

public class UserRunner implements Runner {
    private String stats;
    private Integer userID;

    @Override
    public void run() {
        for (Integer i = new Integer(1); i <= numberOfUsers; i++) {
            pool.execute(new RequestRunner("User:" + i, i));
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