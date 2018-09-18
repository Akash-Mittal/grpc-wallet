package com.betpawa.wallet.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum TRANSACTION {

    DEPOSIT {
        @Override
        public Float doTransact(Integer userID, Float amount, String stats) {
            Float newBalance = null;
            Float existingBalance = wallets.putIfAbsent(userID, amount);
            if (existingBalance != null) {
                newBalance = Float.sum(existingBalance, amount);
                wallets.replace(userID, existingBalance, newBalance);
            } else {
                newBalance = amount;
            }
            System.out.println(Thread.currentThread().getName() + ":" + stats + ":" + DEPOSIT.name() + ":" + amount
                    + BALANCE.name() + ":" + newBalance);

            return newBalance;
        }
    },
    WITHDRAW {
        @Override
        public Float doTransact(Integer userID, Float amount, String stats) {
            Float currenctBalance = wallets.getOrDefault(userID, Float.valueOf(0));
            Float newBalance = currenctBalance;
            if (currenctBalance != null && amount > Float.valueOf(0f) && currenctBalance.compareTo(amount) >= 0) {
                newBalance = currenctBalance - amount;
                wallets.replace(userID, currenctBalance, newBalance);
            }
            System.out.println(Thread.currentThread().getName() + ":" + stats + ":" + WITHDRAW.name() + ":" + amount
                    + BALANCE.name() + ":" + newBalance);

            return newBalance;
        }
    },
    BALANCE {
        @Override
        public Float doTransact(Integer userID, Float amount, String stats) {
            Float balance = wallets.getOrDefault(userID, Float.valueOf(0));
            System.out.println(Thread.currentThread().getName() + ":" + stats + ":" + BALANCE.name() + ":" + balance);
            return balance;

        }
    };

    public abstract Float doTransact(Integer userID, Float amount, String stats);

    private static final Map<Integer, Float> wallets = new ConcurrentHashMap<Integer, Float>();

}