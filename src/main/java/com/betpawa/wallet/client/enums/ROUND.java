package com.betpawa.wallet.client.enums;

import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.WalletServiceGrpc.WalletServiceFutureStub;

public enum ROUND {
    A {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    A.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD,
                    A.name() + ":" + stats);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.EUR,
                    A.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    A.name() + ":" + stats);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, A.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    A.name() + ":" + stats);
        }
    },
    B {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.THREEHUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.GBP,
                    B.name() + ":" + stats);
        }
    },
    C {
        @Override
        public void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats) {
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    C.name() + ":" + stats);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    C.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    C.name() + ":" + stats);
            TRANSACTION.DEPOSIT.doTransact(futureStub, userID, AMOUNT.HUNDERED.getAmount(), CURRENCY.USD,
                    C.name() + ":" + stats);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);
            TRANSACTION.WITHDRAW.doTransact(futureStub, userID, AMOUNT.TWOHUNDERED.getAmount(), CURRENCY.USD,
                    C.name() + ":" + stats);
            TRANSACTION.BALANCE.doTransact(futureStub, userID, null, null, C.name() + ":" + stats);

        }
    };

    public abstract void goExecute(final WalletServiceFutureStub futureStub, final Integer userID, final String stats);

}
