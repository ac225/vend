package com.company.tokens.enums;


import java.util.List;

import com.company.logic.ClientStatus;
import com.company.misc.Actionable;
import com.company.misc.Visitible;

public interface Tokenable {

        void accept(Visitible visitible, List<Product> products, List<Coin> coinsDeposited,
                    List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable);

}
