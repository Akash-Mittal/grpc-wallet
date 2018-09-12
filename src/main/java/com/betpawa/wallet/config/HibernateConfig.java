package com.betpawa.wallet.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.betpawa.wallet.DepositRequest;

public class HibernateConfig {
    private static SessionFactory sessionFactory;
    static {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void main(String[] args) {
        Session session = HibernateConfig.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(DepositRequest.newBuilder().setUserID(1));
        session.getTransaction().commit();
        session.close();
        System.out.println("Done");
    }
}
