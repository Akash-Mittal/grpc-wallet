package com.betpawa.wallet.auto.entities.generated;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * The primary key class for the user_wallet database table.
 * 
 */
@Embeddable
public class UserWalletPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private int walletId;

    @Column(name = "user_id")
    private int userId;

    public UserWalletPK() {
    }

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserWalletPK)) {
            return false;
        }
        UserWalletPK castOther = (UserWalletPK) other;
        return (this.walletId == castOther.walletId) && (this.userId == castOther.userId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.walletId;
        hash = hash * prime + this.userId;

        return hash;
    }

    public static class Builder {
        private int walletId;
        private int userId;

        public Builder walletId(int walletId) {
            this.walletId = walletId;
            return this;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public UserWalletPK build() {
            return new UserWalletPK(this);
        }
    }

    private UserWalletPK(Builder builder) {
        this.walletId = builder.walletId;
        this.userId = builder.userId;
    }
}