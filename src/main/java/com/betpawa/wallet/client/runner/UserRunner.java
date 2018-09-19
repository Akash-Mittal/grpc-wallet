package com.betpawa.wallet.client.runner;

import com.betpawa.wallet.client.domain.ClientParams;

public class UserRunner implements Runner {
    private ClientParams clientParams;

    public ClientParams getClientParams() {
        return clientParams;
    }

    public void setClientParams(ClientParams clientParams) {
        this.clientParams = clientParams;
    }

    @Override
    public void run() {
        for (int i = 1; i <= clientParams.getNumberOfUsers(); i++) {
            clientParams.getPool().execute(new RequestRunner("User:" + i, i, clientParams));
        }
    }

    public UserRunner(ClientParams clientParams) {
        super();
        this.clientParams = clientParams;
    }

}