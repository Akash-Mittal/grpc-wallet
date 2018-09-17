package com.betpawa.wallet.app.dao.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.betpawa.wallet.CURRENCY;

public class CurrencyServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(CURRENCY.USD.name(), SERVICE.FACTORY.getCurrencyService().get(CURRENCY.USD).getCurrencyVal());
    }

}
