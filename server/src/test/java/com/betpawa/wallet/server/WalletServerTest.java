package com.betpawa.wallet.server;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.betpawa.wallet.BalanceRequest;
import com.betpawa.wallet.BalanceResponse;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceBlockingStub;
import com.betpawa.wallet.WithdrawRequest;
import com.betpawa.wallet.WithdrawResponse;
import com.betpawa.wallet.client.enums.runner.Runner;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;

@RunWith(JUnit4.class)
public class WalletServerTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private WalletServer server;
    private ManagedChannel inProcessChannel;
    private WalletServiceBlockingStub stub;

    @Before
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();
        server = new WalletServer(InProcessServerBuilder.forName(serverName).directExecutor(), 0);
        server.start();
        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
        stub = WalletServiceGrpc.newBlockingStub(inProcessChannel);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testDeposit() throws Exception {
        int userID = 1;
        Float amount = 100F;

        for (int i = 0; i < 100; i++) {
            Runner.pool.execute(new Thread() {
                @Override
                public void run() {
                    DepositResponse depositResponse = stub.deposit(
                            DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());

                };
            });

        }
        Runner.pool.awaitTermination(2000, TimeUnit.SECONDS);
        Runner.pool.shutdown();
        // assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        // assertEquals(depositResponse.getUserID(), userID);
        // assertEquals(depositResponse.getCurrency(), CURRENCY.USD);
        //
        // depositResponse = stub
        // .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        // assertEquals(depositResponse.getAmount(), amount * 2.0, 0F);
        // assertEquals(depositResponse.getUserID(), userID);
        // assertEquals(depositResponse.getCurrency(), CURRENCY.USD);
        //
        // depositResponse = stub
        // .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        // assertEquals(depositResponse.getAmount(), amount * 3.0, 0F);
        // assertEquals(depositResponse.getUserID(), userID);
        // assertEquals(depositResponse.getCurrency(), CURRENCY.USD);

    }

    @Test
    public void testDeposit_Zero_Amount() throws Exception {
        int userID = 1;
        Float amount = 0F;
        try {
            stub.deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name(), e.getStatus().getDescription());

        }
    }

    @Test
    public void testWithdraw() throws Exception {
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = stub
                .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        WithdrawResponse withdrawResponse = stub
                .withdraw(WithdrawRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        Assert.assertNotNull(withdrawResponse);
    }

    @Test
    public void testWithdraw_Zero() throws Exception {
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        amount = 0F;
        try {
            withdraw(1, amount, CURRENCY.USD);
        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name(), e.getStatus().getDescription());

        }
    }

    @Test
    public void testWithdraw_Invalid_Request() throws Exception {
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        try {
            stub.withdraw(null);
        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.name(), e.getStatus().getDescription());

        }
    }

    @Test
    public void testWithdraw_Insufficient_Funds() throws Exception {
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        amount = 123.5F;
        try {
            withdraw(userID, amount, CURRENCY.USD);

        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.INSUFFICIENT_BALANCE.name(), e.getStatus().getDescription());

        }
    }

    @Test
    public void testWithdraw_Invalid_User() throws Exception {
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        amount = 123.5F;
        int invalidUserID = 2;
        try {
            withdraw(invalidUserID, amount, CURRENCY.USD);
        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.USER_DOES_NOT_EXIST.name(), e.getStatus().getDescription());

        }
    }

    @Test
    public void testBalance() throws Exception {
        int userID = 1;
        Float amount = 1230F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        Float amountWithdraw = 123.5F;
        WithdrawResponse withdrawResponse = withdraw(userID, amountWithdraw, CURRENCY.USD);
        Assert.assertNotNull(withdrawResponse);
        BalanceResponse balanceResponse = balance(userID);
        assertEquals(balanceResponse.getAmount(), amount - amountWithdraw, 0F);

    }

    @Test
    public void testBalance_User_Not_Exists() throws Exception {
        int userID = 1;
        Float amount = 1230F;
        DepositResponse depositResponse = deposit(userID, amount, CURRENCY.USD);
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        Float amountWithdraw = 123.5F;
        WithdrawResponse withdrawResponse = withdraw(userID, amountWithdraw, CURRENCY.USD);
        Assert.assertNotNull(withdrawResponse);
        userID = -1;
        try {
            balance(userID);
        } catch (StatusRuntimeException e) {
            assertEquals(io.grpc.Status.Code.FAILED_PRECONDITION, e.getStatus().getCode());
            assertEquals(StatusMessage.USER_DOES_NOT_EXIST.name(), e.getStatus().getDescription());

        }
    }

    private DepositResponse deposit(int userID, Float amount, CURRENCY currency) {
        return stub.deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount))
                .setCurrency(currency).build());
    }

    private WithdrawResponse withdraw(int userID, Float amount, CURRENCY currency) {
        return stub.withdraw(
                WithdrawRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build());
    }

    private BalanceResponse balance(int userID) {
        return stub.balance(BalanceRequest.newBuilder().setUserID(userID).build());
    }
}
