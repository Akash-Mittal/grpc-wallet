package com.betpawa.wallet.auto.entities.generated;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the user_wallet database table.
 * 
 */
@Entity
@Table(name = "user_wallet")
@NamedQuery(name = "UserWallet.findAll", query = "SELECT u FROM UserWallet u")
public class UserWallet implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserWalletPK id;

    private float balance;

    private String currency;

    public UserWallet() {
    }

    public UserWalletPK getId() {
        return this.id;
    }

    public void setId(UserWalletPK id) {
        this.id = id;
    }

    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public static class Builder {
        private UserWalletPK id;
        private float balance;
        private String currency;

        public Builder id(UserWalletPK id) {
            this.id = id;
            return this;
        }

        public Builder balance(float balance) {
            this.balance = balance;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public UserWallet build() {
            return new UserWallet(this);
        }
    }

    private UserWallet(Builder builder) {
        this.id = builder.id;
        this.balance = builder.balance;
        this.currency = builder.currency;
    }
}