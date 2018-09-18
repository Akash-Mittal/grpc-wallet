package com.betpawa.wallet.app.dao.service;

import java.util.List;
import java.util.Random;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.StatusMessage;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.auto.entities.generated.UserWalletPK;
import com.betpawa.wallet.enums.FACTORY;

import javassist.NotFoundException;

public class UserWalletServiceImpl {
    private static final Random random = new Random(433);

    public UserWallet getByUserIDCurrency(Integer userID, CURRENCY currency, boolean throwException)
            throws NotFoundException {
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserIDCurrency(userID, currency);
        if (userWallets == null || userWallets.isEmpty() && !throwException) {
            return getDefault(userID, currency);
        } else if (userWallets == null || userWallets.isEmpty() && throwException) {
            throw new NotFoundException(StatusMessage.USER_DOES_NOT_EXIST.name());
        } else {
            return userWallets.get(0);
        }
    }

    private UserWallet getDefault(Integer userID, CURRENCY currency) {
        return new UserWallet.Builder().currency(currency.name()).balance(Float.valueOf(0))
                .id(new UserWalletPK.Builder().userId(userID).walletId(random.nextInt(9999999)).build()).build();
    }

    public List<UserWallet> getByUserID(Integer userID) throws NotFoundException {
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserID(userID);
        if (userWallets == null || userWallets.isEmpty()) {
            throw new NotFoundException(StatusMessage.USER_DOES_NOT_EXIST.name());
        }
        return userWallets;
    }

    public void saveOrUpdate(UserWallet userWallet) {
        FACTORY.GET.userWalletRepo().saveOrUpdate(userWallet);
    }

    public void update(UserWallet userWallet) {
        FACTORY.GET.userWalletRepo().update(userWallet);
    }
}
