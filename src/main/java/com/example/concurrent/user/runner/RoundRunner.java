package com.example.concurrent.user.runner;

import java.util.Random;

import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.enums.ROUND;

public class RoundRunner implements Runner {
    private String stats;
    private Integer userID;

    private Random random = new Random();

    @Override
    public void run() {
        ROUND.values()[random.nextInt(ROUND.values().length)].goExecute(WalletClient.futureStub, userID);
    }

    public RoundRunner(String stats, Integer userID) {
        this.userID = userID;
        this.stats = stats;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public RoundRunner(Integer userID) {
        super();
        this.userID = userID;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

}
