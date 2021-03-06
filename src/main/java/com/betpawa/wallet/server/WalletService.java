package com.betpawa.wallet.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.Balance;
import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceImplBase;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.exception.BPServiceException;
import com.betpawa.wallet.service.UserWalletService;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

@Grpcservice
public class WalletService extends WalletServiceImplBase implements UserWalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Override
    public synchronized void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        Float balanceToADD = request.getAmount();
        try {
            if (checkAmountGreaterThanZero(balanceToADD) && checkCurrency(request.getCurrency())) {
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
                logger.warn(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name() + "OR"
                        + StatusMessage.INVALID_CURRENCY.name());
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name() + "OR"
                                + StatusMessage.INVALID_CURRENCY.name())));
            }
        } catch (BPServiceException e) {
            responseObserver.onError(new StatusRuntimeException(e.getStatus().withDescription(e.getMessage())));

        } catch (Exception e) {
            logger.error(StatusMessage.UNRECOGNIZED.name(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription(StatusMessage.UNRECOGNIZED.name())));
        }
    }

    @Override
    public synchronized void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {

        logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                request.getCurrency());
        try {
            Float balanceToWithdraw = request.getAmount();
            if (checkAmountGreaterThanZero(balanceToWithdraw) && checkCurrency(request.getCurrency())) {
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
                logger.warn(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name() + "OR"
                        + StatusMessage.INVALID_CURRENCY.name());
                responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                        .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name() + "OR"
                                + StatusMessage.INVALID_CURRENCY.name())));
            }
        } catch (BPServiceException e) {
            responseObserver.onError(new StatusRuntimeException(e.getStatus().withDescription(e.getMessage())));

        } catch (Exception e) {
            logger.error(StatusMessage.UNRECOGNIZED.name(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription(StatusMessage.UNRECOGNIZED.name())));
        }
    }

    @Override
    public synchronized void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        logger.info("Request Recieved for UserID:{}", request.getUserID());
        try {
            List<UserWallet> userWallets = getByUserID(request.getUserID());
            List<Balance> balanceList = new ArrayList<>();

            final StringBuilder balance = new StringBuilder();
            userWallets.forEach(wallet -> {
                Balance bl = Balance.newBuilder().setAmount(wallet.getBalance())
                        .setCurrency(CURRENCY.valueOf(wallet.getCurrency())).build();
                balance.append(wallet.getCurrency() + ":" + wallet.getBalance());
                balanceList.add(bl);
            });
            logger.info(balance.toString());
            responseObserver.onNext(BalanceResponse.newBuilder().addAllBalance(balanceList).build());
            responseObserver.onCompleted();
        } catch (BPServiceException e) {
            logger.warn(StatusMessage.USER_DOES_NOT_EXIST.name());
            responseObserver.onError(new StatusRuntimeException(
                    Status.FAILED_PRECONDITION.withDescription(StatusMessage.USER_DOES_NOT_EXIST.name())));

        } catch (Exception e) {
            logger.error(StatusMessage.UNRECOGNIZED.name(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription(StatusMessage.UNRECOGNIZED.name())));
        }

    }

    static boolean checkAmountGreaterThanZero(Float amount) {
        boolean valid = false;
        if (amount > 0F && amount < Float.MAX_VALUE / 2F) {
            valid = true;
        }
        return valid;
    }

    static boolean checkCurrency(CURRENCY currency) {
        boolean valid = true;
        if (currency.equals(CURRENCY.UNRECOGNIZED)) {
            valid = false;
        }
        return valid;
    }

}