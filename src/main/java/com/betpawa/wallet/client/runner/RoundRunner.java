package com.betpawa.wallet.client.runner;

import java.util.concurrent.ThreadLocalRandom;

import com.betpawa.wallet.client.domain.ClientParams;
import com.betpawa.wallet.client.enums.Client;

public class RoundRunner implements Runner {
    private String stats;
    private Integer userID;
    private ClientParams clientParams;

    @Override
    public void run() {
        for (int i = 1; i <= clientParams.getNumberOfRounds(); i++) {
            Client.ROUND.values()[ThreadLocalRandom.current().nextInt(0, (Client.ROUND.values().length))]
                    .goExecute(clientParams.getFutureStub(), userID, stats + ":Round:" + i);
        }
    }

    public RoundRunner(String stats, Integer userID, ClientParams clientParams) {
        super();
        this.stats = stats;
        this.userID = userID;
        this.clientParams = clientParams;
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

    public ClientParams getClientParams() {
        return clientParams;
    }

    public void setClientParams(ClientParams clientParams) {
        this.clientParams = clientParams;
    }

}
