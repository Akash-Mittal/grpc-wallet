package com.betpawa.wallet.app;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.client.domain.ClientParams;
import com.betpawa.wallet.client.runner.UserRunner;
import com.betpawa.wallet.service.WalletService;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class WalletApp {
    private static final Logger logger = LoggerFactory.getLogger(WalletApp.class);
    private static Server server;
    private static ManagedChannel channel;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties props = System.getProperties();
        Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.user", "1"));
        Integer numberOfRequests = Integer.valueOf(props.getProperty("wallet.request", "1"));
        Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.round", "1"));

        WalletApp store = new WalletApp();
        store.startServer();
        try {
            store.runClient(numberOfUsers, numberOfRequests, numberOfRounds);
        } finally {
            store.stopServer();
        }

    }

    private void runClient(Integer numberOfUsers, Integer numberOfRequests, Integer numberOfRounds)
            throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(10);

        try {
            if (channel != null) {
                throw new IllegalStateException("Already started");
            }
            channel = ManagedChannelBuilder.forAddress("localhost", server.getPort()).usePlaintext(true).build();
            ClientParams clientParams = new ClientParams(numberOfUsers, numberOfRequests, numberOfRounds,
                    WalletServiceGrpc.newFutureStub(channel), pool);
            pool.execute(new UserRunner(clientParams));
            pool.awaitTermination(5, TimeUnit.SECONDS);
        } finally {
            pool.shutdown();
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private void startServer() throws IOException {
        if (server != null) {
            throw new IllegalStateException("Already started");
        }
        server = ServerBuilder.forPort(1234).addService(new WalletService()).build();
        server.start();
        logger.info("Server started, listening on " + 1234);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    WalletApp.this.stopServer();
                } catch (InterruptedException e) {
                    System.err.println("*** abnormal server shut down " + e);
                }
                System.err.println("*** server shut down");
            }
        });
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