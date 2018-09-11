package com.betpawa.wallet.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceBlockingStub;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class WalletClient {
    private static final Logger logger = Logger.getLogger(WalletClient.class.getName());
    private final ManagedChannel channel;
    private final WalletServiceBlockingStub blockingStub;
    private AtomicLong rpcCount = new AtomicLong();

    /** Construct client for accessing RouteGuide server at {@code host:port}. */
    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public WalletClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = WalletServiceGrpc.newBlockingStub(channel);
    }

    public WalletClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = WalletServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void deposit(float amount, int userID, CURRENCY currency) {
        try {

            blockingStub.deposit(
                    DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, e.getStatus().getDescription());
        }

    }

    public WithdrawResponse withdraw(int userID, Float amount, CURRENCY currency) {
        WithdrawResponse withdrawResponse = null;
        try {
            withdrawResponse = blockingStub.withdraw(
                    WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());
            rpcCount.incrementAndGet();

        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, e.getStatus().getDescription());
        }
        return withdrawResponse;
    }

    public BalanceResponse balance(int userID) {
        rpcCount.incrementAndGet();
        BalanceResponse balanceResponse = null;
        try {

            balanceResponse = blockingStub.balance(BalanceRequest.newBuilder().setUserID(userID).build());
            rpcCount.incrementAndGet();

        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, e.getStatus().getDescription());
        }
        return balanceResponse;

    }

    public AtomicLong getRpcCount() {
        return rpcCount;
    }

}
