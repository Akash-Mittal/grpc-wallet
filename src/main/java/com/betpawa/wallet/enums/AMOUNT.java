package com.betpawa.wallet.enums;

public enum AMOUNT {
    ZERO(Float.valueOf(0)), HUNDERED(Float.valueOf(100)), TWOHUNDERED(Float.valueOf(200)), THREEHUNDERED(
            Float.valueOf(300)), FOURHUNDERED(Float.valueOf(400)), FIVEHUNDERED(Float.valueOf(5000));

    private Float amount;

    public Float getAmount() {
        return amount;
    }

    private AMOUNT(Float amount) {
        this.amount = amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
