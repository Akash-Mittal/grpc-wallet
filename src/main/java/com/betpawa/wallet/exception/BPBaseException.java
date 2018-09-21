package com.betpawa.wallet.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class BPBaseException extends StatusRuntimeException {
    private static final long serialVersionUID = 4267114442294762693L;
    private static final Logger logger = LoggerFactory.getLogger(BPBaseException.class);

    private String message;

    public BPBaseException(Status status, Metadata trailers) {
        super(status, trailers);
        // TODO Auto-generated constructor stub
    }

    public BPBaseException(String message, Status status) {
        super(status);
        logger.error(message);
        this.setMessage(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
