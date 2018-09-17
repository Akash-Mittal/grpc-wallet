package com.betpawa.wallet.app.dao.service;

import java.util.List;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.auto.entities.generated.UserWalletPK;

public class UserWalletServiceImpl {

    public UserWallet getByUserIDCurrency(Integer userID, CURRENCY currency) {
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserIDCurrency(userID, CURRENCY.USD);
        if (userWallets == null || userWallets.isEmpty()) {
            return getDefault(userID, currency);
        }
        return userWallets.get(0);
    }

    private UserWallet getDefault(Integer userID, CURRENCY currency) {
        return new UserWallet.Builder().currency(currency.name()).balance(Float.valueOf(0))
                .id(new UserWalletPK.Builder().userId(userID).walletId(1333234).build()).build();
    }

    public List<UserWallet> getByUserID(Integer userID) {
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserID(userID);
        if (userWallets == null || userWallets.isEmpty()) {
            // throw
        }
        return userWallets;
    }

    public void saveOrUpdate(UserWallet userWallet) {
        FACTORY.GET.userWalletRepo().saveOrUpdate(userWallet);
    }
}
