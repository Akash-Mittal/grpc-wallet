package com.betpawa.wallet.client.enums.runner;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Runner extends Runnable {
    ExecutorService pool = Executors.newFixedThreadPool(10);
    Properties props = System.getProperties();
    Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.users", "1"));
    Integer numberOfRequests = Integer.valueOf(props.getProperty("wallet.threads", "1"));
    Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.rounds", "1"));

}
