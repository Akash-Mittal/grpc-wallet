package com.betpawa.wallet.enums;

public enum ROUND {
    A {
        @Override
        public void goExecute(Integer userID, String stats) {
            TRANSACTION.BALANCE.doTransact(userID, null, stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.TWOHUNDERED.getAmount(), stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
        }
    },
    B {
        @Override
        public void goExecute(Integer userID, String stats) {
            TRANSACTION.BALANCE.doTransact(userID, null, stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.THREEHUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);

        }
    },
    C {
        @Override
        public void goExecute(Integer userID, String stats) {
            TRANSACTION.BALANCE.doTransact(userID, null, stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.DEPOSIT.doTransact(userID, AMOUNT.HUNDERED.getAmount(), stats);
            TRANSACTION.WITHDRAW.doTransact(userID, AMOUNT.TWOHUNDERED.getAmount(), stats);

        }
    };

    public abstract void goExecute(Integer userID, String stats);

}
