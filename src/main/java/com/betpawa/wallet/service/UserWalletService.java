package com.betpawa.wallet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.auto.entities.generated.UserWallet;
import com.betpawa.wallet.auto.entities.generated.UserWalletPK;
import com.betpawa.wallet.enums.FACTORY;
import com.betpawa.wallet.exception.BPDataException;
import com.betpawa.wallet.exception.BPServiceException;

import io.grpc.Status;
import javassist.NotFoundException;

public interface UserWalletService {

    default UserWallet getByUserIDCurrency(Integer userID, CURRENCY currency, boolean throwException) {
        List<UserWallet> userWallets = new ArrayList<>();
        try {
            userWallets = FACTORY.GET.userWalletRepo().getByUserIDCurrency(userID, currency);
        } catch (BPDataException e) {
            if (throwException) {
                throw new BPServiceException(e.getMessage(), Status.NOT_FOUND);
            } else {
                userWallets.add(getDefault(userID, currency));
            }

        }
        return userWallets.get(0);
    }

    default UserWallet getDefault(Integer userID, CURRENCY currency) {
        // ThreadLocalRandom for Demo only
        return new UserWallet.Builder().currency(currency.name()).balance(Float.valueOf(0))
                .id(new UserWalletPK.Builder().userId(userID).walletId(ThreadLocalRandom.current().nextInt(0, 99999999))
                        .build())
                .build();
    }

    default List<UserWallet> getByUserID(Integer userID) throws NotFoundException {
        List<UserWallet> userWallets = FACTORY.GET.userWalletRepo().getByUserID(userID);
        return userWallets;
    }

    default void saveOrUpdate(UserWallet userWallet) {
        FACTORY.GET.userWalletRepo().saveOrUpdate(userWallet);
    }

    default void update(UserWallet userWallet) {
        FACTORY.GET.userWalletRepo().update(userWallet);
    }
}
