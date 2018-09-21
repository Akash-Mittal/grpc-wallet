package com.betpawa.wallet.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.app.dao.BaseRepositoryImpl;

import io.grpc.Metadata;
import io.grpc.Status;

public class BPDataException extends BPBaseException {

    private static final long serialVersionUID = 4890201354059182901L;
    private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    public BPDataException(Status status, Metadata trailers) {
        super(status, trailers);
    }

    public BPDataException(String message, Status status) {
        super(message, status);

    }

}
