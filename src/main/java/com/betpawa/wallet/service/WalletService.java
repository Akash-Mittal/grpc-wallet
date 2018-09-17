package com.betpawa.wallet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
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

public class WalletService extends WalletServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    // private final Map<Integer, Float> wallets = new ConcurrentHashMap<Integer, Float>();

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        Float balanceToADD = request.getAmount();
        if (checkAmountGreaterThanZero(balanceToADD)) {
            Float currentBalance = Float.valueOf(0);
            logger.info("Request Recieved for UserID:{} For Amount:{}{} ", request.getUserID(), request.getAmount(),
                    request.getCurrency());
            UserWallet userWallet = FACTORY.GET.userWalletService().getByUserIDCurrency(request.getUserID(),
                    request.getCurrency());
            currentBalance = userWallet.getBalance();
            Float newBalance = Float.sum(currentBalance, balanceToADD);
            userWallet.setBalance(newBalance);
            FACTORY.GET.userWalletService().saveOrUpdate(userWallet);
            responseObserver
                    .onNext(DepositResponse.newBuilder().setUserID(request.getUserID()).setAmount(newBalance).build());
            responseObserver.onCompleted();
            logger.info("Wallet Updated SuccessFully New Balance:", newBalance);

        } else {
            logger.warn(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name());
            responseObserver.onError(new StatusRuntimeException(Status.FAILED_PRECONDITION
                    .withDescription(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name())));
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        if (checkAmountGreaterThanZero(request.getAmount())) {
            if (FACTORY.FACTORY.getUserService().get(request.getUserID()) != null) {
                Float balanceToWithdraw = request.getAmount();
                UserWallet userWallet = FACTORY.GET.userWalletService().getByUserIDCurrency(request.getUserID(),
                        request.getCurrency());
                Float existingBalance = userWallet.getBalance();
                if (existingBalance.compareTo(balanceToWithdraw) >= 0) {
                    FACTORY.FACTORY.userWalletRepo().updateBalance(request.getUserID(), request.getCurrency(),
                            existingBalance - balanceToWithdraw);
                    responseObserver.onNext(WithdrawResponse.newBuilder().build());
                    responseObserver.onCompleted();
                } else {
                    logger.warn(StatusMessage.INSUFFICIENT_BALANCE.name());
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
        if (FACTORY.FACTORY.getUserService().get(request.getUserID()) != null) {
            responseObserver.onNext(BalanceResponse.newBuilder()
                    .setAmount(FACTORY.FACTORY.userWalletRepo().getBalance(request.getUserID(), CURRENCY.USD)).build());
            responseObserver.onCompleted();
        } else {
            logger.warn("{}", request.getUserID(), StatusMessage.USER_DOES_NOT_EXIST.name());
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