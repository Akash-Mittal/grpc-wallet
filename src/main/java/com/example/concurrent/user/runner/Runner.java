package com.example.concurrent.user.runner;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Runner extends Runnable {
    ExecutorService pool = Executors.newCachedThreadPool();
    Properties props = System.getProperties();
    Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.users", "2"));
    Integer numberOfRequests = Integer.valueOf(props.getProperty("wallet.threads", "2"));
    Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.rounds", "2"));

}
