package com.betpawa.wallet.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceBlockingStub;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.client.WalletClient;

import io.grpc.StatusRuntimeException;

public enum WalletTransactions {

    DEPOSIT {
        @Override
        public void doTransact(WalletServiceBlockingStub blockingStub, int userID, float amount, CURRENCY currency) {
            try {

                blockingStub.deposit(
                        DepositRequest.newBuilder().setAmount(amount).setUserID(userID).setCurrency(currency).build());

            } catch (StatusRuntimeException e) {
                logger.warn(e.getStatus().getDescription());
            }

        }
    },
    WITHDRAW {
        @Override
        public void doTransact(WalletServiceBlockingStub blockingStub, int userID, float amount, CURRENCY currency) {
            try {

                blockingStub.withdraw(
                        WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());

            } catch (StatusRuntimeException e) {
                logger.warn(e.getStatus().getDescription());
            }

        }
    },
    BALANCE {
        @Override
        public void doTransact(WalletServiceBlockingStub blockingStub, int userID, float amount, CURRENCY currency) {
            try {

                blockingStub.balance(BalanceRequest.newBuilder().setUserID(userID).build());
            } catch (StatusRuntimeException e) {
                logger.warn(e.getStatus().getDescription());
            }

        }
    };

    public abstract void doTransact(WalletServiceBlockingStub blockingStub, int userID, float amount,
            CURRENCY currency);

    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);

}
