package com.betpawa.wallet.app.dao.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.betpawa.wallet.auto.entities.generated.BpUser;

public class UserServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGet() throws Exception {
        BpUser user = SERVICE.FACTORY.getUserService().get(1);
        assertNotNull(user);
        assertEquals(1, user.getUserId());

    }

}
