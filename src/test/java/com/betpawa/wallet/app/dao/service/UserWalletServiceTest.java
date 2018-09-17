package com.betpawa.wallet.app.dao.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.betpawa.wallet.CURRENCY;

public class UserWalletServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetBalance() throws Exception {
        Float balance = SERVICE.FACTORY.getUserWalletService().getBalance(1, CURRENCY.USD);
        assertNotNull(balance);
        balance = SERVICE.FACTORY.getUserWalletService().getBalance(1, CURRENCY.GBP);
        assertNull(balance);
    }

}
