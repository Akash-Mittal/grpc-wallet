package com.betpawa.wallet.client.runner;

import com.betpawa.wallet.client.WalletClientParams;

public class RequestRunner implements Runner {
    private String stats;
    private Integer userID;
    private WalletClientParams clientParams;

    @Override
    public void run() {
        for (int i = 1; i <= clientParams.getNumberOfRequests(); i++) {
            clientParams.getPool().execute(new RoundRunner(stats + ":Request Number:" + i, userID, clientParams));
        }
    }

    public WalletClientParams getClientParams() {
        return clientParams;
    }

    public void setClientParams(WalletClientParams clientParams) {
        this.clientParams = clientParams;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public RequestRunner(String stats, Integer userID, WalletClientParams clientParams) {
        super();
        this.stats = stats;
        this.userID = userID;
        this.clientParams = clientParams;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

}