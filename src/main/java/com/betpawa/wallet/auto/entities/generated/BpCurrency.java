package com.betpawa.wallet.auto.entities.generated;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * The persistent class for the bp_currency database table.
 * 
 */
@Entity
@Table(name = "bp_currency")
@Immutable
@NamedQuery(name = "BpCurrency.findAll", query = "SELECT b FROM BpCurrency b")
public class BpCurrency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private int currencyId;

    @Column(name = "currency_val")
    private String currencyVal;

    // bi-directional many-to-one association to BpUserCurrency
    @OneToMany(mappedBy = "bpCurrency")
    private List<BpUserCurrency> bpUserCurrencies;

    public BpCurrency() {
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyVal() {
        return this.currencyVal;
    }

    public void setCurrencyVal(String currencyVal) {
        this.currencyVal = currencyVal;
    }

    public List<BpUserCurrency> getBpUserCurrencies() {
        return this.bpUserCurrencies;
    }

    public void setBpUserCurrencies(List<BpUserCurrency> bpUserCurrencies) {
        this.bpUserCurrencies = bpUserCurrencies;
    }

    public BpUserCurrency addBpUserCurrency(BpUserCurrency bpUserCurrency) {
        getBpUserCurrencies().add(bpUserCurrency);
        bpUserCurrency.setBpCurrency(this);

        return bpUserCurrency;
    }

    public BpUserCurrency removeBpUserCurrency(BpUserCurrency bpUserCurrency) {
        getBpUserCurrencies().remove(bpUserCurrency);
        bpUserCurrency.setBpCurrency(null);

        return bpUserCurrency;
    }

    public static class Builder {
        private String currencyVal;

        public Builder currencyVal(String currencyVal) {
            this.currencyVal = currencyVal;
            return this;
        }

        public BpCurrency build() {
            return new BpCurrency(this);
        }
    }

    private BpCurrency(Builder builder) {
        this.currencyVal = builder.currencyVal;
    }
}