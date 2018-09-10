package com.betpawa.wallet.server;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceImplBase;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.betpawa.wallet.exception.WithDrawException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class WalletServer {
    private static final Logger logger = Logger.getLogger(WalletServer.class.getName());

    private final int port;
    private final Server server;

    public WalletServer(int port) throws IOException {
        this(ServerBuilder.forPort(port), port);
    }

    public WalletServer(ServerBuilder<?> serverBuilder, int port) {
        this.port = port;
        // server = null;
        server = serverBuilder.addService(new WalletService()).build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                WalletServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method. This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        WalletServer server = new WalletServer(8980);
        server.start();
        server.blockUntilShutdown();
    }

    private static final class WalletService extends WalletServiceImplBase {

        private final Map<Integer, Float> wallets = new ConcurrentHashMap<>();

        @Override
        public void deposit(DepositRequest depostiRequest, StreamObserver<DepositResponse> responseObserver) {
            Float balanceToADD = depostiRequest.getAmount();
            Float currentBalance = 0F;
            logger.info(
                    "Server Recieved Request forUserID: " + depostiRequest.getUserID() + " Amount: " + balanceToADD);

            if (userExists(depostiRequest.getUserID())) {
                currentBalance = wallets.get(depostiRequest.getUserID());
            }
            Float newBalance = Float.sum(currentBalance, balanceToADD);
            wallets.put(depostiRequest.getUserID(), newBalance);
            logger.info("Server Updated Wallet Succesfully,UserID: " + depostiRequest.getUserID() + "New Amount: "
                    + newBalance);
            responseObserver.onNext(
                    DepositResponse.newBuilder().setUserID(depostiRequest.getUserID()).setAmount(newBalance).build());
            responseObserver.onCompleted();

        }

        @Override
        public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
            if (!Objects.isNull(request)) {
                if (userExists(request.getUserID())) {
                    Float balanceToWithdraw = request.getAmount();
                    Float existingBalance = wallets.get(request.getUserID());
                    if (existingBalance.compareTo(balanceToWithdraw) >= 0) {
                        if (wallets.replace(request.getUserID(), existingBalance,
                                existingBalance - balanceToWithdraw)) {
                            responseObserver.onNext(WithdrawResponse.newBuilder().build());
                            responseObserver.onCompleted();
                        }
                    } else {
                        responseObserver.onError(new WithDrawException(StatusMessage.INSUFFICIENT_BALANCE));
                    }
                } else {
                    responseObserver.onError(new WithDrawException(StatusMessage.USER_DOES_NOT_EXIST));
                }

            } else {
                responseObserver.onError(new WithDrawException(StatusMessage.INVALID_ARGUMENTS));
            }

        }

        @Override
        public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
            if (!Objects.isNull(request)) {
                if (userExists(request.getUserID())) {
                    responseObserver
                            .onNext(BalanceResponse.newBuilder().setAmount(wallets.get(request.getUserID())).build());
                    responseObserver.onCompleted();
                }
            }
        }

        private boolean userExists(int userID) {
            boolean exists = false;
            if (wallets.containsKey(userID)) {
                exists = true;
            }
            return exists;
        }
    }

}
