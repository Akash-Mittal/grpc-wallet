package com.betpawa.wallet.auto.entities.generated;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the bp_user_wallet database table.
 * 
 */
@Entity
@Table(name = "bp_user_wallet")
@NamedQuery(name = "BpUserWallet.findAll", query = "SELECT b FROM BpUserWallet b")
public class BpUserWallet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_wallet_id")
    private int userWalletId;

    @Column(name = "user_balance")
    private float userBalance;

    // bi-directional many-to-one association to BpUserCurrency
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_currency_id")
    private BpUserCurrency bpUserCurrency;

    public BpUserWallet() {
    }

    public int getUserWalletId() {
        return this.userWalletId;
    }

    public void setUserWalletId(int userWalletId) {
        this.userWalletId = userWalletId;
    }

    public float getUserBalance() {
        return this.userBalance;
    }

    public void setUserBalance(float userBalance) {
        this.userBalance = userBalance;
    }

    public BpUserCurrency getBpUserCurrency() {
        return this.bpUserCurrency;
    }

    public void setBpUserCurrency(BpUserCurrency bpUserCurrency) {
        this.bpUserCurrency = bpUserCurrency;
    }

    public static class Builder {
        private float userBalance;
        private BpUserCurrency bpUserCurrency;

        public Builder userBalance(float userBalance) {
            this.userBalance = userBalance;
            return this;
        }

        public Builder bpUserCurrency(BpUserCurrency bpUserCurrency) {
            this.bpUserCurrency = bpUserCurrency;
            return this;
        }

        public BpUserWallet build() {
            return new BpUserWallet(this);
        }
    }

    private BpUserWallet(Builder builder) {
        this.userBalance = builder.userBalance;
        this.bpUserCurrency = builder.bpUserCurrency;
    }
}