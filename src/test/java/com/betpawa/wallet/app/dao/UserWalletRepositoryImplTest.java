package com.betpawa.wallet.app.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.app.dao.service.FACTORY;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.auto.entities.generated.UserWalletPK;

public class UserWalletRepositoryImplTest {

    public void setUp() throws Exception {
        Float balance = Float.valueOf(22);
        Integer userID = Integer.valueOf(1);

        FACTORY.GET.userWalletRepo().save(new UserWallet.Builder().currency(CURRENCY.EUR.name()).balance(balance)
                .id(new UserWalletPK.Builder().userId(userID).walletId(1234).build()).build());

        FACTORY.GET.userWalletRepo().save(new UserWallet.Builder().currency(CURRENCY.USD.name()).balance(balance)
                .id(new UserWalletPK.Builder().userId(userID).walletId(1235).build()).build());

        FACTORY.GET.userWalletRepo().save(new UserWallet.Builder().currency(CURRENCY.GBP.name()).balance(balance)
                .id(new UserWalletPK.Builder().userId(userID).walletId(1236).build()).build());

    }

    // @AfterClass
    // public static void tear() {
    // FACTORY.GET.userWalletRepo().deleteAll(UserWallet.class);
    // }

    @Test
    public void testGetByUserIDCurrency() throws Exception {
        Float balance = Float.valueOf(22);
        Integer userID = Integer.valueOf(1);
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserIDCurrency(userID, CURRENCY.USD);
        Assert.assertNotNull(userWallets);
        Assert.assertEquals(1, userWallets.size());
        assertEquals(balance, userWallets.get(0).getBalance(), Float.valueOf(0));
    }

    @Test
    public void testGetByUserID() throws Exception {
        Integer userID = Integer.valueOf(1);
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserID(userID, CURRENCY.USD);
        Assert.assertNotNull(userWallets);
        Assert.assertEquals(3, userWallets.size());

    }

}
