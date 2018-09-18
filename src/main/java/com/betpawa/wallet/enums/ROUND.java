package com.betpawa.wallet.enums;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;

public enum ROUND {
    A {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID) {
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.EUR);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
        }
    },
    B {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID) {
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.THREEHUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP);
        }
    },
    C {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID) {
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null);

        }
    };

    public abstract void goExecute(final WalletServiceFutureStub futureStub, final Integer userID);

}
