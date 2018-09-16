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

/**
 * The persistent class for the bp_user database table.
 * 
 */
@Entity
@Table(name = "bp_user")
@NamedQuery(name = "BpUser.findAll", query = "SELECT b FROM BpUser b")
public class BpUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String userName;

    // bi-directional many-to-one association to BpUserCurrency
    @OneToMany(mappedBy = "bpUser")
    private List<BpUserCurrency> bpUserCurrencies;

    public BpUser() {
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<BpUserCurrency> getBpUserCurrencies() {
        return this.bpUserCurrencies;
    }

    public void setBpUserCurrencies(List<BpUserCurrency> bpUserCurrencies) {
        this.bpUserCurrencies = bpUserCurrencies;
    }

    public BpUserCurrency addBpUserCurrency(BpUserCurrency bpUserCurrency) {
        getBpUserCurrencies().add(bpUserCurrency);
        bpUserCurrency.setBpUser(this);

        return bpUserCurrency;
    }

    public BpUserCurrency removeBpUserCurrency(BpUserCurrency bpUserCurrency) {
        getBpUserCurrencies().remove(bpUserCurrency);
        bpUserCurrency.setBpUser(null);

        return bpUserCurrency;
    }

    public static class Builder {
        private String userName;

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public BpUser build() {
            return new BpUser(this);
        }
    }

    private BpUser(Builder builder) {
        this.userName = builder.userName;
    }
}