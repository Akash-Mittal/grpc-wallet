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
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.client.enums.Client;
import com.betpawa.wallet.exception.BPServiceException;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;

public class WalletService extends WalletServiceImplBase implements UserWalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Override
    public synchronized void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        Float balanceToADD = request.getAmount();
        try {
            if (Client.checkAmountGreaterThanZero(balanceToADD)) {
                Float currentBalance = Float.valueOf(0);
                UserWallet userWallet;

                logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                        request.getCurrency());
                userWallet = getByUserIDCurrency(request.getUserID(), request.getCurrency(), false);
                currentBalance = userWallet.getBalance();
                Float newBalance = Float.sum(currentBalance, balanceToADD);
                userWallet.setBalance(newBalance);
                saveOrUpdate(userWallet);
                responseObserver.onNext(
                        DepositResponse.newBuilder().setUserID(request.getUserID()).setAmount(newBalance).build());
                responseObserver.onCompleted();
                logger.info("Wallet Updated SuccessFully New Balance:{}", newBalance);

            } else {
                logger.warn(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name());
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
            }
        } catch (BPServiceException e) {
            responseObserver.onError(new StatusRuntimeException(e.getStatus().withDescription(e.getMessage())));
        } catch (Exception e) {
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription(StatusMessage.UNRECOGNIZED.name())));
        }
    }

    @Override
    public synchronized void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {

        logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                request.getCurrency());
        try {

            if (Client.checkAmountGreaterThanZero(request.getAmount())) {
                Float balanceToWithdraw = request.getAmount();
                UserWallet userWallet = getByUserIDCurrency(request.getUserID(), request.getCurrency(), true);
                Float existingBalance = userWallet.getBalance();
                if (existingBalance.compareTo(balanceToWithdraw) >= 0) {
                    Float newBalance = existingBalance - balanceToWithdraw;
                    userWallet.setBalance(newBalance);
                    update(userWallet);
                    responseObserver.onNext(WithdrawResponse.newBuilder().setBalance(newBalance)
                            .setCurrency(request.getCurrency()).build());
                    responseObserver.onCompleted();
                    logger.info("Wallet Updated SuccessFully New Balance:{}", newBalance);

                } else {
                    logger.warn(StatusMessage.INSUFFICIENT_BALANCE.name());
                    responseObserver.onError(new StatusRuntimeException(
                            Status.FAILED_PRECONDITION.withDescription(StatusMessage.INSUFFICIENT_BALANCE.name())));
                }
            } else {
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
            }
        } catch (BPServiceException e) {
            responseObserver.onError(new StatusRuntimeException(e.getStatus().withDescription(e.getMessage())));
        } catch (Exception e) {
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription(StatusMessage.UNRECOGNIZED.name())));
        }
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        logger.info("Request Recieved for UserID:{}", request.getUserID());
        try {
            List<UserWallet> userWallets = getByUserID(request.getUserID());
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

}