package com.betpawa.wallet.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.betpawa.wallet.DepositRequest;
import com.betpawa.wallet.DepositResponse;
import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;

@RunWith(JUnit4.class)
public class WalletServerTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private WalletServer server;
    private ManagedChannel inProcessChannel;

    @Before
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();
        server = new WalletServer(InProcessServerBuilder.forName(serverName).directExecutor(), 0);
        server.start();
        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testDeposit() throws Exception {
        WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(inProcessChannel);
        int userID = 1;
        Float amount = 123F;
        DepositResponse depositResponse = stub
                .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        assertEquals(depositResponse.getAmount(), (Float.sum(amount, 0F)), 0F);
        depositResponse = stub
                .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        assertEquals(depositResponse.getAmount(), amount * 2.0, 0F);

        depositResponse = stub
                .deposit(DepositRequest.newBuilder().setUserID(userID).setAmount(new Float(amount)).build());
        assertEquals(depositResponse.getAmount(), amount * 3.0, 0F);

    }

}
