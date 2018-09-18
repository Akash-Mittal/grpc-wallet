package com.betpawa.wallet.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class BaseException extends StatusRuntimeException {

    public BaseException(Status status) {
        super(status);
    }

}
