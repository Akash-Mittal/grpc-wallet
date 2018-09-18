package com.betpawa.wallet.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.betpawa.wallet.client.WalletClient;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public enum TRANSACTION {

    DEPOSIT {
        @Override
        public void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
                final CURRENCY currency) {
            try {

                ListenableFuture<DepositResponse> response = futureStub.deposit(
                        DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());
                // response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
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
    },
    WITHDRAW {
        @Override
        public void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
                final CURRENCY currency) {
            ListenableFuture<WithdrawResponse> response = null;
            response = futureStub.withdraw(
                    WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());
            // response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
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
    },
    BALANCE {
        @Override
        public void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
                final CURRENCY currency) {
            ListenableFuture<BalanceResponse> response = null;

            response = futureStub.balance(BalanceRequest.newBuilder().setUserID(userID).build());
            // response.addListener(() -> rpcCount.incrementAndGet(), MoreExecutors.directExecutor());
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
    };

    public abstract void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
            final CURRENCY currency);

    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);

}
