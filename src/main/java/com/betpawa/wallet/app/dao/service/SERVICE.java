package com.betpawa.wallet.app.dao.service;

// Eager Initialization could be Lazy as well
public enum SERVICE {

    FACTORY;
    public UserService getUserService() {
        return USER_SERVICE;
    }

    public CurrencyService getCurrencyService() {
        return CURRENCY_SERVICE;
    }

    public UserCurrencyService getUserCurrencyService() {
        return USER_CURRENCY_SERVICE;
    }

    public UserWalletService getUserWalletService() {
        return USER_WALLET_SERVICE;
    }

    private static final UserService USER_SERVICE = new UserService();
    private static final CurrencyService CURRENCY_SERVICE = new CurrencyService();
    private static final UserCurrencyService USER_CURRENCY_SERVICE = new UserCurrencyService();
    private static final UserWalletService USER_WALLET_SERVICE = new UserWalletService();
}
