package com.company.tokens.enums;


import com.company.misc.Actionable;

import java.util.List;

import com.company.logic.ClientStatus;
import com.company.misc.Money;
import com.company.misc.Visitible;

public enum Product implements Tokenable {

    A("GET-A", Money.dollars("0.65")),
    B("GET-B", Money.dollars("1.00")),
    C("GET-C", Money.dollars("1.50"));

    final public String commandWord;
    final public Money price;

    Product(String commandWord, Money price) {
        this.commandWord = commandWord;
        this.price = price;
    }

	@Override
	public void accept(Visitible v, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {

		v.visit(this, products, coinsDeposited,
                refundDue, maintenances, actionable);
	}

}

