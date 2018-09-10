package com.betpawa.wallet.exception;

import com.betpawa.wallet.StatusCode;
import com.betpawa.wallet.StatusMessage;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private StatusCode statusCode;
    private StatusMessage statusMessage;

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }

    public BaseException(StatusCode statusCode, StatusMessage statusMessage) {

        super();
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;

    }

    public BaseException(StatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public BaseException(StatusMessage statusMessage) {
        super();
        this.statusMessage = statusMessage;
    }

}
