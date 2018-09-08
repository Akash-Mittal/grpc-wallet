package com.betpawa.wallet.client;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WalletClient {
    private static final Logger logger = Logger.getLogger(WalletClient.class.getName());

    private final ManagedChannel channel;
    private final WalletServiceBlockingStub blockingStub;

    /** Construct client for accessing RouteGuide server at {@code host:port}. */
    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public WalletClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = WalletServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void deposit(float amount, int userID, CURRENCY currency) {
        logger.info("Client Issued Request to Deposit " + amount + " " + currency.name() + " For userID " + userID);
        DepositResponse depositResponse = blockingStub
                .deposit(DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());
        if (!Objects.isNull(depositResponse))
            logger.info("Client Issued Request Success ");

    }

    public static void main(String[] args) throws InterruptedException {
        WalletClient walletClient = new WalletClient("localhost", 8980);
        try {
            walletClient.deposit(100F, 1, CURRENCY.USD);
            walletClient.deposit(100F, 1, CURRENCY.USD);
            walletClient.deposit(100F, 1, CURRENCY.USD);
        } finally {
            walletClient.shutdown();
        }
    }
}
