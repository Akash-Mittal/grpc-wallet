package com.betpawa.wallet.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.betpawa.wallet.service.WalletService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

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
    public void blockUntilShutdown() throws InterruptedException {
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

}
