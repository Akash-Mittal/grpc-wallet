package com.betpawa.wallet.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class WalletClientAsync {
    private static final Logger logger = LoggerFactory.getLogger(WalletClientAsync.class);
    private final ManagedChannel channel;
    private final WalletServiceFutureStub futureStub;

    private AtomicLong rpcCount = new AtomicLong();

    /** Construct client for accessing RouteGuide server at {@code host:port}. */
    public WalletClientAsync(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public WalletClientAsync(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public WalletClientAsync(ManagedChannel channel) {
        this.channel = channel;
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void deposit(float amount, int userID, CURRENCY currency) {
        try {

            ListenableFuture<DepositResponse> response = futureStub.deposit(
                    DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());
            response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
            Futures.addCallback(response, new FutureCallback<DepositResponse>() {
                @Override
                public void onSuccess(DepositResponse result) {
                    logger.info("Deposited Succesfully" + result);
                }

                @Override
                public void onFailure(Throwable t) {
                    logger.warn(Status.fromThrowable(t).getDescription());
                }
            });
        } catch (StatusRuntimeException e) {

        }

    }

    public void withdraw(int userID, Float amount, CURRENCY currency) {
        ListenableFuture<WithdrawResponse> response = null;
        response = futureStub.withdraw(
                WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());
        response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
        Futures.addCallback(response, new FutureCallback<WithdrawResponse>() {
            @Override
            public void onSuccess(WithdrawResponse result) {
                logger.info("Withdrawn Succesfully" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                logger.warn(Status.fromThrowable(t).getDescription());
            }
        });

    }

    public void balance(int userID) {
        ListenableFuture<BalanceResponse> response = null;

        response = futureStub.balance(BalanceRequest.newBuilder().setUserID(userID).build());
        response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
        Futures.addCallback(response, new FutureCallback<BalanceResponse>() {
            @Override
            public void onSuccess(BalanceResponse result) {
                logger.info("Balance Checked Succesfully" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                logger.warn(Status.fromThrowable(t).getDescription());
            }
        });
    }

    public AtomicLong getRpcCount() {
        return rpcCount;
    }

}
