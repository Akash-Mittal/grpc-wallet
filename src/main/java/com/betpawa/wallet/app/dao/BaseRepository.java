package com.betpawa.wallet.app.dao;

import java.util.List;
import java.util.Map;

public interface BaseRepository<T> {
    public T get(Class<T> cl, Integer id);

    public T save(T object);

    public void update(T object);

    public void saveOrUpdate(T object);

    public void delete(T object);

    public List<T> query(String hsql, Map<String, Object> params) throws BPDataException;

    public List<T> getAll(Class<T> cl) throws BPDataException;

    public void deleteAll(Class<T> cl) throws BPDataException;
}
