package com.betpawa.wallet.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceImplBase;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class WalletService extends WalletServiceImplBase {
    private static final Logger logger = Logger.getLogger(WalletService.class.getName());

    private final Map<Integer, Float> wallets = new ConcurrentHashMap<Integer, Float>();

    @Override
    public void deposit(DepositRequest depositRequest, StreamObserver<DepositResponse> responseObserver) {
        Float balanceToADD = depositRequest.getAmount();
        if (checkAmountGreaterThanZero(balanceToADD)) {
            Float currentBalance = 0F;
            logger.info(
                    "Server Recieved Request forUserID: " + depositRequest.getUserID() + " Amount: " + balanceToADD);

            if (userExists(depositRequest.getUserID())) {
                currentBalance = wallets.get(depositRequest.getUserID());
            }
            Float newBalance = Float.sum(currentBalance, balanceToADD);
            wallets.put(depositRequest.getUserID(), newBalance);
            logger.info("Server Updated Wallet Succesfully,UserID: " + depositRequest.getUserID() + " New Amount: "
                    + newBalance);
            responseObserver.onNext(
                    DepositResponse.newBuilder().setUserID(depositRequest.getUserID()).setAmount(newBalance).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                    .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        if (checkAmountGreaterThanZero(request.getAmount())) {
            if (userExists(request.getUserID())) {
                Float balanceToWithdraw = request.getAmount();
                Float existingBalance = wallets.get(request.getUserID());
                if (existingBalance.compareTo(balanceToWithdraw) >= 0) {
                    if (wallets.replace(request.getUserID(), existingBalance, existingBalance - balanceToWithdraw)) {
                        responseObserver.onNext(WithdrawResponse.newBuilder().build());
                        responseObserver.onCompleted();
                    }
                } else {
                    logger.log(Level.WARNING, StatusMessage.INSUFFICIENT_BALANCE.name());
                    responseObserver.onError(new StatusRuntimeException(
                            Status.FAILED_PRECONDITION.withDescription(StatusMessage.INSUFFICIENT_BALANCE.name())));
                }
            } else {
                responseObserver.onError(new StatusRuntimeException(
                        Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));
            }
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                    .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
        }

    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        if (userExists(request.getUserID())) {
            responseObserver.onNext(BalanceResponse.newBuilder().setAmount(wallets.get(request.getUserID())).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusRuntimeException(
                    Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));
        }
    }

    private boolean userExists(int userID) {
        boolean exists = false;
        if (wallets.containsKey(userID)) {
            exists = true;
        }
        return exists;
    }

    private boolean checkAmountGreaterThanZero(Float amount) {
        boolean valid = false;

        if (amount > 0F && amount < Float.MAX_VALUE / 2F) {
            valid = true;
        }
        return valid;
    }
}