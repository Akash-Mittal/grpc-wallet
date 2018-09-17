package com.betpawa.wallet.auto.entities.generated;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the bp_user_currency database table.
 * 
 */
@Entity
@Table(name = "bp_user_currency")
@NamedQuery(name = "BpUserCurrency.findAll", query = "SELECT b FROM BpUserCurrency b")
public class BpUserCurrency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_currency_id")
    private int userCurrencyId;

    // bi-directional many-to-one association to BpCurrency
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private BpCurrency bpCurrency;

    // bi-directional many-to-one association to BpUser
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private BpUser bpUser;

    // bi-directional many-to-one association to BpUserWallet
    @OneToMany(mappedBy = "bpUserCurrency")
    private List<BpUserWallet> bpUserWallets;

    public BpUserCurrency() {
    }

    public int getUserCurrencyId() {
        return this.userCurrencyId;
    }

    public void setUserCurrencyId(int userCurrencyId) {
        this.userCurrencyId = userCurrencyId;
    }

    public BpCurrency getBpCurrency() {
        return this.bpCurrency;
    }

    public void setBpCurrency(BpCurrency bpCurrency) {
        this.bpCurrency = bpCurrency;
    }

    public BpUser getBpUser() {
        return this.bpUser;
    }

    public void setBpUser(BpUser bpUser) {
        this.bpUser = bpUser;
    }

    public List<BpUserWallet> getBpUserWallets() {
        return this.bpUserWallets;
    }

    public void setBpUserWallets(List<BpUserWallet> bpUserWallets) {
        this.bpUserWallets = bpUserWallets;
    }

    public BpUserWallet addBpUserWallet(BpUserWallet bpUserWallet) {
        getBpUserWallets().add(bpUserWallet);
        bpUserWallet.setBpUserCurrency(this);

        return bpUserWallet;
    }

    public BpUserWallet removeBpUserWallet(BpUserWallet bpUserWallet) {
        getBpUserWallets().remove(bpUserWallet);
        bpUserWallet.setBpUserCurrency(null);

        return bpUserWallet;
    }

    public static class Builder {
        private BpCurrency bpCurrency;
        private BpUser bpUser;

        public Builder bpCurrency(BpCurrency bpCurrency) {
            this.bpCurrency = bpCurrency;
            return this;
        }

        public Builder bpUser(BpUser bpUser) {
            this.bpUser = bpUser;
            return this;
        }

        public BpUserCurrency build() {
            return new BpUserCurrency(this);
        }
    }

    private BpUserCurrency(Builder builder) {
        this.bpCurrency = builder.bpCurrency;
        this.bpUser = builder.bpUser;
    }

}