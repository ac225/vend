package com.company.misc;


import java.util.List;

import com.company.logic.ClientStatus;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Misc;
import com.company.tokens.enums.Product;

public interface Visitible {

    void visit(Product product, List<Product> products, List<Coin> coinsDeposited,
               List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable);
    void visit(Misc misc, List<Product> products, List<Coin> coinsDeposited,
               List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable);
    void visit(Coin coin, List<Product> products, List<Coin> coinsDeposited,
               List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable);
    void visit(Maintenance maintenance, List<Product> products, List<Coin> coinsDeposited,
               List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable);

}
