package com.betpawa.wallet.client.enums.runner;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.betpawa.wallet.client.WalletClient;
import com.betpawa.wallet.client.enums.runner.RoundRunner;
import com.betpawa.wallet.client.enums.runner.Runner;
import com.betpawa.wallet.server.WalletServer;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;

public class RoundRunnerTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private WalletServer server;
    private ManagedChannel inProcessChannel;
    private WalletClient walletClient;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        server = new WalletServer(InProcessServerBuilder.forName(serverName).directExecutor(), 0);
        server.start();
        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
        walletClient = new WalletClient(inProcessChannel);
    }

    @Test
    public void testRun() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            Runner.pool.execute(new RoundRunner(i));
        }
        Runner.pool.awaitTermination(1, TimeUnit.SECONDS);
        Runner.pool.shutdown();

    }

}
