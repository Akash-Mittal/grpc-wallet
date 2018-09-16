package com.betpawa.wallet.app.dao.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.BpUserCurrency;
import com.betpawa.wallet.config.HibernateConfig;

public class UserCurrencyService extends GenericServiceImpl<BpUserCurrency> {

    public UserCurrencyService(Class<BpUserCurrency> cl, SessionFactory sessionFactory) {
        super(cl, sessionFactory);
    }

    public UserCurrencyService(SessionFactory sessionFactory) {
        super(BpUserCurrency.class, sessionFactory);
    }

    public UserCurrencyService() {
        super(BpUserCurrency.class, HibernateConfig.getSessionFactory());
    }

    public Integer getID(int userID, CURRENCY currency) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Integer result = (Integer) session.createCriteria(BpUserCurrency.class).add(Restrictions.eq("user_id", userID))
                .add(Restrictions.eq("currency_id", SERVICE.FACTORY.getCurrencyService().get(currency).getCurrencyId()))
                .setProjection(Property.forName("user_currency_id")).uniqueResult();
        return result;

    }

}