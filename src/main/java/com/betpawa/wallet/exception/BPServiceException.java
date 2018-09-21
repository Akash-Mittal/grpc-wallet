package com.betpawa.wallet.exception;

import io.grpc.Metadata;
import io.grpc.Status;

public class BPServiceException extends BPBaseException {

    private static final long serialVersionUID = 4890201354059182901L;

    public BPServiceException(Status status, Metadata trailers) {
        super(status, trailers);
    }

    public BPServiceException(String message, Status status) {
        super(message, status);
    }

}
