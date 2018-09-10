package com.betpawa.wallet.exception;

import com.betpawa.wallet.StatusCode;
import com.betpawa.wallet.StatusMessage;

public class WithDrawException extends BaseException {

    private static final long serialVersionUID = 7325002811054568732L;

    public WithDrawException(StatusCode statusCode, StatusMessage statusMessage) {
        super(statusCode, statusMessage);
    }

    public WithDrawException(StatusCode statusCode) {
        super(statusCode);
    }

    public WithDrawException(StatusMessage statusMessage) {
        super(statusMessage);
    }

}
