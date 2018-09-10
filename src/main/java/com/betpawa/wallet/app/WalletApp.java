package com.betpawa.wallet.app;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.service.WalletService;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class WalletApp {
    private static final Logger logger = Logger.getLogger(WalletApp.class.getName());

    private static final long DURATION_SECONDS = 60;

    private Server server;
    private ManagedChannel channel;

    public static void main(String[] args) throws InterruptedException, IOException {
        WalletApp store = new WalletApp();
        store.startServer();
        try {
            store.runClient();
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

    private void runClient() throws InterruptedException {
        if (channel != null) {
            throw new IllegalStateException("Already started");
        }
        channel = ManagedChannelBuilder.forAddress("localhost", server.getPort()).usePlaintext(true).build();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        try {
            AtomicBoolean done = new AtomicBoolean();
            WalletClient client = new WalletClient(channel);
            logger.info("Starting");
            scheduler.schedule(() -> done.set(true), DURATION_SECONDS, TimeUnit.SECONDS);
            while (!done.get()) {
                RoundA(client);
            }
            double qps = client.getRpcCount().longValue() / DURATION_SECONDS;
            logger.log(Level.INFO, "Did {0} RPCs/s", new Object[] { qps });
        } finally {
            scheduler.shutdownNow();
            channel.shutdownNow();
        }
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
