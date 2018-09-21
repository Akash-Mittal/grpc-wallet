package com.betpawa.wallet.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betpawa.wallet.app.dao.UserWalletRepositoryImpl;

// Eager Initialization could be Lazy as well
public enum FACTORY {

    GET;

    public UserWalletRepositoryImpl userWalletRepo() {
        try {
            return USER_WALLET_REPO;
        } catch (Exception e) {
            logger.error("Error Initializing Database", e);
        }
        return null;
    }

    private static final UserWalletRepositoryImpl USER_WALLET_REPO = new UserWalletRepositoryImpl();
    private static final Logger logger = LoggerFactory.getLogger(FACTORY.class);

}
