package com.betpawa.wallet.enums;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.client.enums.ROUND;
import com.betpawa.wallet.server.WalletServer;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;

public class ROUNDTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private WalletServer server;
    private ManagedChannel inProcessChannel;
    private WalletClient walletClient;

    @Before
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();
        server = new WalletServer(InProcessServerBuilder.forName(serverName).directExecutor(), 0);
        server.start();
        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
        walletClient = new WalletClient(inProcessChannel);
    }

    @Test
    public void test() {
        ROUND.B.goExecute(walletClient.getFutureStub(), 1);
    }
}
