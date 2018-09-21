package com.betpawa.wallet.client;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;
import com.betpawa.wallet.client.domain.ClientParams;
import com.betpawa.wallet.client.runner.UserRunner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WalletClient {
    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);
    private final ManagedChannel channel;
    private final WalletServiceFutureStub futureStub;

    private AtomicLong rpcCount = new AtomicLong();

    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public WalletClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public WalletClient(ManagedChannel channel) {
        this.channel = channel;
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public AtomicLong getRpcCount() {
        return rpcCount;
    }

    public WalletServiceFutureStub getFutureStub() {
        return futureStub;
    }

    public static void main(String[] args) {
        final ExecutorService pool = Executors.newFixedThreadPool(10);

        try {
            Properties props = System.getProperties();
            Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.user", "1"));
            Integer numberOfRequests = Integer.valueOf(props.getProperty("wallet.request", "1"));
            Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.round", "1"));

            WalletClient client = new WalletClient("localhost", 1234);
            ClientParams clientParams = new ClientParams(numberOfUsers, numberOfRequests, numberOfRounds,
                    client.futureStub, pool);
            pool.execute(new UserRunner(clientParams));
            pool.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            logger.error("Excpetion while Closing thread pool", e);
        } finally {
            pool.shutdownNow();
        }
    }
}
