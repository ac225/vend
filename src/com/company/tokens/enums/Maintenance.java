package com.company.tokens.enums;


import com.company.misc.Actionable;

import java.util.List;

import com.company.logic.ClientStatus;
import com.company.misc.Visitible;

public enum Maintenance implements Tokenable {

    WORKING(""),
    SERVICE("SERVICE");
    final public String commandWord;

    Maintenance(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public void accept(Visitible v, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {
        v.visit(this, products, coinsDeposited,
                refundDue, maintenances, actionable);
    }
}



