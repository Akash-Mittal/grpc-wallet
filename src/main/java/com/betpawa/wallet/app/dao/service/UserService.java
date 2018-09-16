package com.betpawa.wallet.app.dao.service;

import org.hibernate.SessionFactory;

import com.betpawa.wallet.auto.entities.generated.BpUser;
import com.betpawa.wallet.config.HibernateConfig;

public class UserService extends GenericServiceImpl<BpUser> {

    public UserService(Class<BpUser> cl, SessionFactory sessionFactory) {
        super(cl, sessionFactory);
    }

    public UserService(SessionFactory sessionFactory) {
        super(BpUser.class, sessionFactory);
    }

    public UserService() {
        super(BpUser.class, HibernateConfig.getSessionFactory());
    }

    public BpUser get(Integer id) {
        return super.get(BpUser.class, id);
    }

    public BpUser userExists(int userID) {
        BpUser user = get(userID);
        if ((user = SERVICE.FACTORY.getUserService().get(userID)) != null) {
            return user;
        }
        return null;
    }

}
