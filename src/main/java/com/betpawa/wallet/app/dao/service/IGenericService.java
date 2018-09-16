package com.betpawa.wallet.app.dao.service;

import java.util.List;

import com.betpawa.wallet.app.dao.IGenericDAO;

public interface IGenericService<T> extends IGenericDAO<T> {
    List<T> getAll();

    void deleteAll();
}
