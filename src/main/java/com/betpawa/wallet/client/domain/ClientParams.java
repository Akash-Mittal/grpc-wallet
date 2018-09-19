package com.betpawa.wallet.client.domain;

import java.util.concurrent.ExecutorService;

import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;

public final class ClientParams {

    private final Integer numberOfUsers;
    private final Integer numberOfRequests;
    private final Integer numberOfRounds;
    private final WalletServiceFutureStub futureStub;
    private final ExecutorService pool;

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public ClientParams(Integer numberOfUsers, Integer numberOfRequests, Integer numberOfRounds,
            WalletServiceFutureStub futureStub, ExecutorService pool) {
        super();
        this.numberOfUsers = numberOfUsers;
        this.numberOfRequests = numberOfRequests;
        this.numberOfRounds = numberOfRounds;
        this.futureStub = futureStub;
        this.pool = pool;
    }

    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public Integer getNumberOfRounds() {
        return numberOfRounds;
    }

    public WalletServiceFutureStub getFutureStub() {
        return futureStub;
    }

    public ExecutorService getPool() {
        return pool;
    }

}
