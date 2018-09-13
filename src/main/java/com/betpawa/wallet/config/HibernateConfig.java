package com.betpawa.wallet.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.BpCurrency;
import com.betpawa.wallet.auto.entities.generated.BpUser;

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
        BpCurrency bpCurrency = new BpCurrency();
        bpCurrency.setCurrencyVal(CURRENCY.USD.name());

        session.save(bpCurrency);
        session.save(new BpUser());
        session.getTransaction().commit();
        session.close();
        System.out.println("Done");
    }
}
