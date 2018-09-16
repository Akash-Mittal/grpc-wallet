package com.betpawa.wallet.app.dao.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.BpUserWallet;
import com.betpawa.wallet.config.HibernateConfig;

public class UserWalletService extends GenericServiceImpl<BpUserWallet> {

    public UserWalletService(Class<BpUserWallet> cl, SessionFactory sessionFactory) {
        super(cl, sessionFactory);
    }

    public UserWalletService(SessionFactory sessionFactory) {
        super(BpUserWallet.class, sessionFactory);
    }

    public UserWalletService() {
        super(BpUserWallet.class, HibernateConfig.getSessionFactory());
    }

    public void add() {

    }

    public Float getBalance(int userID, CURRENCY currency) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Float balance = (Float) session.createCriteria(BpUserWallet.class)
                .add(Restrictions.eq("user_currency_id",
                        SERVICE.FACTORY.getUserCurrencyService().getID(userID, currency)))
                .setProjection(Property.forName("user_balance")).uniqueResult();

        session.getTransaction().commit();
        return balance;
    }

    public Float updateBalance(int userID, CURRENCY currency, Float newBalance) {
        Integer userCurrencyID = SERVICE.FACTORY.getUserCurrencyService().getID(userID, currency);
        BpUserWallet bpUserWallet = getByUserCurrencyID(userCurrencyID);
        update(bpUserWallet);
        return newBalance;
    }

    public BpUserWallet getByUserCurrencyID(Integer userCurrencyID) {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        String queryString = "from BpUserWallet wallet where bpUserCurrency=" + userCurrencyID;
        Query query = session.createQuery(queryString);
        return (BpUserWallet) query.list().get(0);

    }

}