package com.betpawa.wallet.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.config.HibernateConfig;
import com.betpawa.wallet.exception.BPDataException;

public class UserWalletRepositoryImpl extends BaseRepositoryImpl<UserWallet> {

    public UserWalletRepositoryImpl(Class<UserWallet> cl, SessionFactory sessionFactory) {
        super(cl, sessionFactory);
    }

    public UserWalletRepositoryImpl(SessionFactory sessionFactory) {
        super(UserWallet.class, sessionFactory);
    }

    public UserWalletRepositoryImpl() {

        super(UserWallet.class, HibernateConfig.getSessionFactory());
    }

    public List<UserWallet> getByUserIDCurrency(Integer userID, CURRENCY currency) throws BPDataException {

        Map<String, Object> params = new HashMap<>();
        params.put("userID", userID);
        params.put("currency", currency.name());
        List<UserWallet> userWallets = super.query("from UserWallet where user_id = :userID and currency =:currency",
                params);
        super.validate(userWallets);
        return userWallets;
    }

    public List<UserWallet> getByUserID(Integer userID) throws BPDataException {
        Map<String, Object> params = new HashMap<>();
        params.put("userID", userID);
        List<UserWallet> userWallets = super.query("from UserWallet where user_id = :userID", params);
        super.validate(userWallets);
        return userWallets;
    }

}