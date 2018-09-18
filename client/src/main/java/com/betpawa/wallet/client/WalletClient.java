package com.betpawa.wallet.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WalletClient {
    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);
    private final ManagedChannel channel;
    public static WalletServiceFutureStub futureStub;

    private AtomicLong rpcCount = new AtomicLong();

    /** Construct client for accessing RouteGuide server at {@code host:port}. */
    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
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

}
