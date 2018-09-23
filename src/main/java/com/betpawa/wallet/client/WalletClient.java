package com.betpawa.wallet.client;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.WalletServiceGrpc;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;
import com.betpawa.wallet.client.runner.UserRunner;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WalletClient implements Client {
    private static final Logger logger = LoggerFactory.getLogger(WalletClient.class);
    private final ManagedChannel channel;
    private final WalletServiceFutureStub futureStub;
    private TestHelper testHelper;

    private AtomicLong rpcCount = new AtomicLong();

    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public WalletClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public WalletClient(ManagedChannel channel) {
        this.channel = channel;
        futureStub = WalletServiceGrpc.newFutureStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public AtomicLong getRpcCount() {
        return rpcCount;
    }

    public WalletServiceFutureStub getFutureStub() {
        return futureStub;
    }

    public static void main(String[] args) {
        try {

            start("192.168.99.100", 1234, System.getProperties());
            // start("localhost", 1234, System.getProperties());
        } catch (Exception e) {
            logger.error("Excpetion while Starting Wallet Client", e);
        } finally {
            logger.info("Status of this Execution ");

        }
    }

    public static void start(String host, int port, final Properties props) throws InterruptedException {

        final ExecutorService pool = Executors.newFixedThreadPool(10);
        WalletClient client = null;
        try {
            logger.info("Starting client at host {} port {}", host, port);
            Integer numberOfUsers = Integer.valueOf(props.getProperty("wallet.user", "1"));
            Integer numberOfRequests = Integer.valueOf(props.getProperty("wallet.request", "1"));
            Integer numberOfRounds = Integer.valueOf(props.getProperty("wallet.round", "1"));
            client = new WalletClient(host, port);
            final WalletClientParams clientParams = new WalletClientParams(numberOfUsers, numberOfRequests,
                    numberOfRounds, client.futureStub, pool);
            logger.info("Executing User Requests With:{}", clientParams);
            logger.info("Client Will Terminate in:{} {} ", Client.getOptimizedWaitingTime(clientParams),
                    TimeUnit.SECONDS.name());

            pool.execute(new UserRunner(clientParams));
            pool.awaitTermination(Client.getOptimizedWaitingTime(clientParams), TimeUnit.SECONDS);
            logger.info("************** SHUTTING NOW************");
            pool.shutdown();
        } catch (Exception e) {
            logger.error("Exception while Starting Wallet Client", e);
            throw e;
        } finally {
            pool.shutdownNow();
            if (client != null) {
                client.shutdown();
            }
        }
    }

    /**
     * Only used for helping unit test.
     */
    @VisibleForTesting
    interface TestHelper {
        /**
         * Used for verify/inspect message received from server.
         */
        void onMessage(Message message);

        /**
         * Used for verify/inspect error received from server.
         */
        void onRpcError(Throwable exception);
    }

    @VisibleForTesting
    void setTestHelper(TestHelper testHelper) {
        this.testHelper = testHelper;
    }

}
