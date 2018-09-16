package com.betpawa.wallet.app.dao.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.BpCurrency;
import com.betpawa.wallet.config.HibernateConfig;

public class CurrencyService extends GenericServiceImpl<BpCurrency> {
    private static final List<BpCurrency> currencyList;

    static {
        currencyList = SERVICE.FACTORY.getCurrencyService().getAll();
    }

    public CurrencyService(Class<BpCurrency> cl, SessionFactory sessionFactory) {
        super(cl, sessionFactory);
    }

    public CurrencyService(SessionFactory sessionFactory) {
        super(BpCurrency.class, sessionFactory);
    }

    public CurrencyService() {
        super(BpCurrency.class, HibernateConfig.getSessionFactory());
    }

    public BpCurrency get(CURRENCY currency) {
        Optional<BpCurrency> bpCurrency = null;
        bpCurrency = currencyList.stream().filter(val -> val.getCurrencyVal().equals(currency.name())).findFirst();
        return bpCurrency.get();
    }
}