package com.betpawa.wallet.enums;

import com.betpawa.wallet.app.dao.UserWalletRepositoryImpl;
import com.betpawa.wallet.app.dao.service.UserWalletServiceImpl;

// Eager Initialization could be Lazy as well
public enum FACTORY {

    GET;

    public UserWalletRepositoryImpl userWalletRepo() {
        return USER_WALLET_REPO;
    }

    public UserWalletServiceImpl userWalletService() {
        return USER_WALLET_SERVICE;
    }

    private static final UserWalletRepositoryImpl USER_WALLET_REPO = new UserWalletRepositoryImpl();
    private static final UserWalletServiceImpl USER_WALLET_SERVICE = new UserWalletServiceImpl();

}
