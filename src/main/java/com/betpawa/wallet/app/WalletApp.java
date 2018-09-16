package com.betpawa.wallet.app;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.client.WalletClientAsync;
import com.betpawa.wallet.service.WalletService;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class WalletApp {
    private static final Logger logger = LoggerFactory.getLogger(WalletApp.class);

    private static final long DURATION_SECONDS = 10;

    private Server server;
    private ManagedChannel channel;

    public static void main(String[] args) throws InterruptedException, IOException {
        WalletApp store = new WalletApp();
        store.startServer();
        try {
            store.runSimpleClient();
            // store.runAysncClient();
        } finally {
            store.stopServer();
        }
    }

    private void startServer() throws IOException {
        if (server != null) {
            throw new IllegalStateException("Already started");
        }
        server = ServerBuilder.forPort(8980).addService(new WalletService()).build();
        server.start();
    }

    private void stopServer() throws InterruptedException {
        Server s = server;
        if (s == null) {
            throw new IllegalStateException("Already stopped");
        }
        server = null;
        s.shutdown();
        if (s.awaitTermination(1, TimeUnit.SECONDS)) {
            return;
        }
        s.shutdownNow();
        if (s.awaitTermination(1, TimeUnit.SECONDS)) {
            return;
        }
        throw new RuntimeException("Unable to shutdown server");
    }

    private void runSimpleClient() throws InterruptedException {
        if (channel != null) {
            throw new IllegalStateException("Already started");
        }
        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort()).usePlaintext(true).build();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            AtomicBoolean done = new AtomicBoolean();
            WalletClient client = new WalletClient(channel);
            WalletClientAsync walletClientAsync = new WalletClientAsync(channel);
            logger.info("Starting");
            scheduler.schedule(() -> done.set(true), DURATION_SECONDS, TimeUnit.SECONDS);
            while (!done.get()) {
                RoundA(client);
                // ARoundA(walletClientAsync);
            }
            double qps = client.getRpcCount().longValue() / DURATION_SECONDS;
            logger.info("Did {} RPCs/s", qps);
        } finally {
            scheduler.shutdownNow();
            channel.shutdownNow();
        }
    }

    private void runAysncClient() throws InterruptedException {
        if (channel != null) {
            throw new IllegalStateException("Already started");
        }
        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort()).usePlaintext(true).build();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            AtomicBoolean done = new AtomicBoolean();
            WalletClientAsync client = new WalletClientAsync(channel);
            logger.info("Starting");
            scheduler.schedule(() -> done.set(true), DURATION_SECONDS, TimeUnit.SECONDS);
            while (!done.get()) {
                ARoundA(client);
            }
            double qps = client.getRpcCount().longValue() / DURATION_SECONDS;
            logger.info("Did {} RPCs/s", qps);
        } finally {

            scheduler.shutdownNow();
            channel.shutdownNow();
        }
    }

    private void ARoundA(WalletClientAsync client) {
        client.deposit(100F, 1, CURRENCY.USD);
        client.withdraw(1, 200F, CURRENCY.USD);
        client.deposit(100F, 1, CURRENCY.EUR);
    }

    private void RoundA(WalletClient client) {
        client.deposit(100F, 1, CURRENCY.USD);
        client.withdraw(1, 200F, CURRENCY.USD);
        client.deposit(100F, 1, CURRENCY.EUR);
        client.balance(1);
        client.withdraw(1, 100F, CURRENCY.USD);
        client.balance(1);
        client.withdraw(1, 100F, CURRENCY.USD);
    }

}

class Exec extends Thread {
    private WalletClient client;

    public Exec(WalletClient client) {
        super();
        this.client = client;
    }

    @Override
    public void run() {
        client.deposit(100F, 1, CURRENCY.USD);
        client.withdraw(1, 200F, CURRENCY.USD);
        client.deposit(100F, 1, CURRENCY.EUR);
        client.balance(1);
        client.withdraw(1, 100F, CURRENCY.USD);
        client.balance(1);
        client.withdraw(1, 100F, CURRENCY.USD);
    }

    public WalletClient getClient() {
        return client;
    }

    public void setClient(WalletClient client) {
        this.client = client;
    }
}

class RunArguments {
    private Integer numberOfUsers = Integer.valueOf(1);
    private Integer numberOfThread = Integer.valueOf(1);
    private Integer numberOfRounds = Integer.valueOf(1);

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public RunArguments setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
        return this;
    }

    public Integer getNumberOfThread() {
        return numberOfThread;
    }

    public RunArguments setNumberOfThread(Integer numberOfThread) {
        this.numberOfThread = numberOfThread;
        return this;
    }

    public Integer getNumberOfRounds() {
        return numberOfRounds;
    }

    public RunArguments setNumberOfRounds(Integer numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        return this;
    }

}