package com.betpawa.wallet.app.dao.service;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.betpawa.wallet.app.dao.GenericDAOImpl;
import com.betpawa.wallet.app.dao.IGenericDAO;

public class GenericServiceImpl<T> implements IGenericService<T> {

    private IGenericDAO<T> dao;
    private Class<T> cl;

    public GenericServiceImpl(Class<T> cl, SessionFactory sessionFactory) {
        this.cl = cl;
        dao = new GenericDAOImpl<T>(cl, sessionFactory);
    }

    @Override
    public T get(Class<T> cl, Integer id) {
        return dao.get(cl, id);
    }

    @Override
    public T save(T object) {
        return dao.save(object);
    }

    @Override
    public void update(T object) {
        dao.update(object);
    }

    @Override
    public void saveOrUpdate(T object) {
        dao.saveOrUpdate(object);
    }

    @Override
    public void delete(T object) {
        dao.delete(object);
    }

    @Override
    public List<T> query(String hsql, Map<String, Object> params) {
        return dao.query(hsql, params);
    }

    @Override
    public List<T> getAll() {
        return query("from " + cl.getName(), null);
    }

    @Override
    public void deleteAll() {
        query("delete from " + cl.getName(), null);

    }

}
