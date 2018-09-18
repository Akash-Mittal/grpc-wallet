package com.betpawa.wallet.app;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.service.WalletService;
import com.example.concurrent.user.runner.Runner;
import com.example.concurrent.user.runner.UserRunner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class WalletApp {
    private static final Logger logger = LoggerFactory.getLogger(WalletApp.class);

    private static final long DURATION_SECONDS = 1;

    private static Server server;
    private static ManagedChannel channel;

    public static void main(String[] args) throws InterruptedException, IOException {
        WalletApp store = new WalletApp();
        store.startServer();
        try {
            if (channel != null) {
                throw new IllegalStateException("Already started");
            }
            channel = ManagedChannelBuilder.forAddress("localhost", server.getPort()).usePlaintext(true).build();
            WalletClient walletClient = new WalletClient(channel);

            Properties props = System.getProperties();
            Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.users", "10"));
            Integer numberOfThread = Integer.valueOf(props.getProperty("wallet.threads", "1"));
            Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.rounds", "100"));

            for (int user = 1; user <= numberOfUsers; user++) {
                Thread thread = new Thread(new UserRunner("", user));
                Runner.pool.execute(thread);
            }
            Runner.pool.awaitTermination(5, TimeUnit.SECONDS);
            Runner.pool.shutdown();
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

}