package com.betpawa.wallet.app.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.exception.BPDataException;

import io.grpc.Status;

public class BaseRepositoryImpl<T> implements BaseRepository<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    private SessionFactory sessionFactory;

    public BaseRepositoryImpl(Class<T> clazz, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        if (sessionFactory == null)
            throw new RuntimeException("Session factory is null!!!");
    }

    @Override
    public T get(Class<T> clazz, Integer id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        T element = (T) session.get(clazz, id);
        session.getTransaction().commit();
        return element;
    }

    @Override
    public T save(T object) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
        return object;
    }

    @Override
    public void update(T object) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(object);
        session.getTransaction().commit();
    }

    @Override
    public void saveOrUpdate(T object) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
    }

    @Override
    public void delete(T object) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> query(String hsql, Map<String, Object> params) throws BPDataException {
        Session session = null;
        List<T> result = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            Query query = session.createQuery(hsql);
            if (params != null) {
                for (String i : params.keySet()) {
                    query.setParameter(i, params.get(i));
                }
            }
            if ((hsql.toUpperCase().indexOf("DELETE") == -1) && (hsql.toUpperCase().indexOf("UPDATE") == -1)
                    && (hsql.toUpperCase().indexOf("INSERT") == -1)) {
                result = query.list();
            } else {
            }
            session.getTransaction().commit();
        } catch (JDBCConnectionException e) {
            throw new BPDataException("Error While Getting new Database Connection:", Status.DATA_LOSS);
        } catch (Exception e) {
            throw new BPDataException("Unkown Error While Getting Querying Database:", Status.DATA_LOSS);
        }
        return result;

    }

    @Override
    public List<T> getAll(Class<T> clazz) throws BPDataException {
        return query("from " + clazz.getName(), null);
    }

    @Override
    public void deleteAll(Class<T> clazz) throws BPDataException {
        query("delete from " + clazz.getName(), null);

    }

    public void validate(final List<UserWallet> userWallets) {
        if (userWallets == null) {
            throw new BPDataException("List is Null", Status.DATA_LOSS);
        }
        if (userWallets.isEmpty()) {
            throw new BPDataException("List is Empty", Status.DATA_LOSS);
        }

    }

}
