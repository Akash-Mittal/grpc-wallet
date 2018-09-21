package com.betpawa.wallet.client.runner;

import com.betpawa.wallet.client.WalletClientParams;

public class UserRunner implements Runner {
    private WalletClientParams clientParams;

    public WalletClientParams getClientParams() {
        return clientParams;
    }

    public void setClientParams(WalletClientParams clientParams) {
        this.clientParams = clientParams;
    }

    @Override
    public void run() {
        for (int i = 1; i <= clientParams.getNumberOfUsers(); i++) {
            clientParams.getPool().execute(new RequestRunner("User:" + i, i, clientParams));
        }
    }

    public UserRunner(WalletClientParams clientParams) {
        super();
        this.clientParams = clientParams;
    }

}