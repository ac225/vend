package com.company.tokens.enums;


import com.company.misc.Actionable;
import com.company.logic.ClientStatus;
import com.company.misc.Money;
import com.company.misc.Visitible;

import java.math.BigDecimal;
import java.util.*;


public enum Coin implements Tokenable {

    NICKEL(Money.dollars(new BigDecimal(0.05)), "N"),
    DIME(Money.dollars(new BigDecimal(0.10)), "D"),
    QUARTER(Money.dollars(new BigDecimal(0.25)), "Q"),
    DOLLAR(Money.dollars(new BigDecimal(1.00)), "DOLLAR");

    final public Money value;
    final public String commandWord;

    Coin(Money value, String commandWord) {
        this.value = value;
        this.commandWord = commandWord;
    }

    @Override
    public void accept(Visitible v, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {
        v.visit(this, products, coinsDeposited,
                refundDue, maintenances, actionable);

    }
    public static List<Coin> reversedSortedByCoinValue(){

        List<Coin> enums = new ArrayList<>(Arrays.asList(Coin.values()));
        Collections.sort(enums, (coin, otherCoin) -> otherCoin.value.compareTo(coin.value));
        return enums;
    }

    @Override
    public String toString() {
        return commandWord;
    }
}

