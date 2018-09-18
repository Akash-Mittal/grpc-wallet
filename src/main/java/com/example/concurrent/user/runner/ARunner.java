package com.example.concurrent.user.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ARunner {
    private final ExecutorService pool;
    private final Integer numberOfUsers;
    private final Integer numberOfRequests;
    private final Integer numberOfRounds;

    public ARunner(Integer numberOFThreads, Integer numberOfUsers, Integer numberOfRequests, Integer numberOfRounds) {
        super();
        this.pool = Executors.newFixedThreadPool(numberOFThreads);
        this.numberOfUsers = numberOfUsers;
        this.numberOfRequests = numberOfRequests;
        this.numberOfRounds = numberOfRounds;
    }

    public ExecutorService getPool() {
        return pool;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public Integer getNumberOfRounds() {
        return numberOfRounds;
    }

    public void run() {

    }

}
