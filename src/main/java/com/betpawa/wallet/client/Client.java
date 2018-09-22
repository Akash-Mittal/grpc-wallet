package com.betpawa.wallet.client;

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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.Status;

public interface Client {

    int MEAN_FACTOR = 3;
    int DEFAULT_WAIT_TIME = 1;
    int AVERAGE_RPC_PER_ROUND = 7;

    enum AMOUNT {
        ZERO(Float.valueOf(0)), HUNDERED(Float.valueOf(100)), TWOHUNDERED(Float.valueOf(200)), THREEHUNDERED(
                Float.valueOf(300)), FOURHUNDERED(Float.valueOf(400)), FIVEHUNDERED(Float.valueOf(5000));

        private Float amount;

        public Float getAmount() {
            return amount;
        }

        private AMOUNT(Float amount) {
            this.amount = amount;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }
    }

    enum ROUND {
        A {
            @Override
            public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        A.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD,
                        A.name() + ":" + stats);
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.EUR,
                        A.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        A.name() + ":" + stats);
                TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, A.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        A.name() + ":" + stats);
            }
        },
        B {
            @Override
            public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.THREEHUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                        B.name() + ":" + stats);
            }
        },
        C {
            @Override
            public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
                TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        C.name() + ":" + stats);
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        C.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        C.name() + ":" + stats);
                TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                        C.name() + ":" + stats);
                TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);
                TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD,
                        C.name() + ":" + stats);
                TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);

            }
        };

        public abstract void goExecute(final WalletServiceFutureStub futureStub, final Integer userID,
                final String stats);

    }

    enum TRANSACTION {

        DEPOSIT {
            @Override
            public void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
                    final CURRENCY currency, final String stats) {
                logger.info(stats + DEPOSIT.name());

                ListenableFuture<DepositResponse> response = futureStub.deposit(
                        DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());
                Futures.addCallback(response, new FutureCallback<DepositResponse>() {
                    @Override
                    public void onSuccess(DepositResponse result) {

                        logger.info("Deposited Succesfully", result.getCurrencyValue());

                    }

                    @Override
                    public void onFailure(Throwable t) {

                        logger.warn(Status.fromThrowable(t).getDescription());
                    }
                });

            }
        },
        WITHDRAW {
            @Override
            public void doTransact(final WalletServiceFutureStub futureStub, final Integer userID, final Float amount,
                    final CURRENCY currency, final String stats) {
                logger.info(stats + WITHDRAW.name());

                ListenableFuture<WithdrawResponse> response = null;
                response = futureStub.withdraw(
                        WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());
                Futures.addCallback(response, new FutureCallback<WithdrawResponse>() {
                    @Override
                    public void onSuccess(WithdrawResponse result) {

                        logger.info("Withdrawn Succesfully" + result.getBalance());
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
                    final CURRENCY currency, final String stats) {
                logger.info(stats + BALANCE.name());
                ListenableFuture<BalanceResponse> response = futureStub
                        .balance(BalanceRequest.newBuilder().setUserID(userID).build());
                Futures.addCallback(response, new FutureCallback<BalanceResponse>() {
                    @Override
                    public void onSuccess(BalanceResponse result) {
                        logger.info("Balance Checked for user:{} Amount:{}", userID, buildGetBalanceLogLine(result));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.warn(Status.fromThrowable(t).getDescription());
                    }
                });

            }
        };

        public abstract void doTransact(final WalletServiceFutureStub futureStub, final Integer userID,
                final Float amount, final CURRENCY currency, final String stats);

        private static final Logger logger = LoggerFactory.getLogger(TRANSACTION.class);

    }

    static Long getOptimizedWaitingTime(final WalletClientParams clientParams) {
        Long defaultWaitTime = Long.valueOf(DEFAULT_WAIT_TIME);
        defaultWaitTime = Long.valueOf((clientParams.getNumberOfUsers() * clientParams.getNumberOfRequests()
                * clientParams.getNumberOfRounds() * AVERAGE_RPC_PER_ROUND) / MEAN_FACTOR);
        return defaultWaitTime;
    }

    static boolean checkAmountGreaterThanZero(Float amount) {
        boolean valid = false;
        if (amount > 0F && amount < Float.MAX_VALUE / 2F) {
            valid = true;
        }
        return valid;
    }

    static void pingServer() {
        // For Health Check
    }

    static String buildGetBalanceLogLine(BalanceResponse balanceResponse) {
        StringBuilder stringBuilder = new StringBuilder();
        balanceResponse.getBalanceList().stream().forEach(balance -> {
            stringBuilder.append(balance.getAmount()).append(balance.getCurrency().name()).append(" ");
        });
        return stringBuilder.toString();
    }
}