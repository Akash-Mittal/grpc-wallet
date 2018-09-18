package com.betpawa.wallet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceImplBase;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.betpawa.wallet.app.dao.service.FACTORY;
import com.betpawa.wallet.auto.entities.generated.UserWallet;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;

public class WalletService extends WalletServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        try {
            Float balanceToADD = request.getAmount();
            if (checkAmountGreaterThanZero(balanceToADD)) {
                Float currentBalance = Float.valueOf(0);
                UserWallet userWallet;

                logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                        request.getCurrency());
                userWallet = FACTORY.GET.userWalletService().getByUserIDCurrency(request.getUserID(),
                        request.getCurrency(), false);

                currentBalance = userWallet.getBalance();
                Float newBalance = Float.sum(currentBalance, balanceToADD);
                userWallet.setBalance(newBalance);
                FACTORY.GET.userWalletService().saveOrUpdate(userWallet);
                responseObserver.onNext(
                        DepositResponse.newBuilder().setUserID(request.getUserID()).setAmount(newBalance).build());
                responseObserver.onCompleted();
                logger.info("Wallet Updated SuccessFully New Balance:", newBalance);

            } else {
                logger.warn(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name());
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
            }
        } catch (NotFoundException e) {
            logger.warn(StatusMessage.USER_DOES_NOT_EXIST.name());
            responseObserver.onError(new StatusRuntimeException(
                    Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {

        logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                request.getCurrency());
        try {
            if (checkAmountGreaterThanZero(request.getAmount())) {
                // if (FACTORY.GET.getUserService().get(request.getUserID()) != null) {
                Float balanceToWithdraw = request.getAmount();
                UserWallet userWallet = FACTORY.GET.userWalletService().getByUserIDCurrency(request.getUserID(),
                        request.getCurrency(), true);
                Float existingBalance = userWallet.getBalance();
                if (existingBalance.compareTo(balanceToWithdraw) >= 0) {
                    userWallet.setBalance(existingBalance - balanceToWithdraw);
                    FACTORY.GET.userWalletService().update(userWallet);
                    responseObserver.onNext(WithdrawResponse.newBuilder().build());
                    responseObserver.onCompleted();
                } else {
                    logger.warn(StatusMessage.INSUFFICIENT_BALANCE.name());
                    responseObserver.onError(new StatusRuntimeException(
                            Status.FAILED_PRECONDITION.withDescription(StatusMessage.INSUFFICIENT_BALANCE.name())));
                }
            } else {
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
            }
        } catch (NotFoundException notFoundException) {

        }
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        logger.info("Request Recieved for UserID:{}", request.getUserID());
        try {
            List<UserWallet> userWallets = FACTORY.GET.userWalletService().getByUserID(request.getUserID());
            final StringBuilder balance = new StringBuilder();
            userWallets.forEach(wallet -> {
                balance.append(wallet.getCurrency() + ":" + wallet.getBalance());
            });
            logger.info(balance.toString());
            responseObserver.onNext(BalanceResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            logger.warn(StatusMessage.USER_DOES_NOT_EXIST.name());

            responseObserver.onError(new StatusRuntimeException(
                    Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));

        }

    }

    private boolean checkAmountGreaterThanZero(Float amount) {
        boolean valid = false;

        if (amount > 0F && amount < Float.MAX_VALUE / 2F) {
            valid = true;
        }
        return valid;
    }

}