package com.betpawa.wallet.client.enums.runner;

import java.util.concurrent.ThreadLocalRandom;

import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.client.enums.ROUND;

public class RoundRunner implements Runner {
    private String stats;
    private Integer userID;

    @Override
    public void run() {
        for (Integer i = new Integer(1); i <= numberOfRounds; i++) {
            ROUND.values()[ThreadLocalRandom.current().nextInt(0, (ROUND.values().length))]
                    .goExecute(WalletClient.futureStub, userID, stats + ":Round:" + i);
        }

    }

    public RoundRunner(String stats, Integer userID) {
        super();
        this.stats = stats;
        this.userID = userID;
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
