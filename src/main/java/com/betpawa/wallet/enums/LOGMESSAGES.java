package com.betpawa.wallet.enums;

enum LOGMESSAGES {
    REQUEST_RECIEVED(" Request Recieved For User{} To Add Amount {} ");
    private String message;

    private LOGMESSAGES(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}