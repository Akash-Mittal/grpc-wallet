package com.betpawa.wallet.client.enums;

import com.betpawa.wallet.app.dao.UserWalletRepositoryImpl;
import com.betpawa.wallet.service.WalletService;

// Eager Initialization could be Lazy as well
public enum FACTORY {

    GET;

    public UserWalletRepositoryImpl userWalletRepo() {
        return USER_WALLET_REPO;
    }

    public WalletService userWalletService() {
        return WALLET_SERVICE;
    }

    private static final UserWalletRepositoryImpl USER_WALLET_REPO = new UserWalletRepositoryImpl();
    private static final WalletService WALLET_SERVICE = new WalletService();

}
