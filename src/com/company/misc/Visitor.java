package com.company.misc;

import java.util.List;

import com.company.logic.ClientStatus;
import com.company.logic.CoinRegister;
import com.company.logic.DisplayCabinet;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Misc;
import com.company.tokens.enums.Product;

public class Visitor implements Visitible {

    DisplayCabinet displayCabinet;
    CoinRegister coinRegister;

    @Override
    public void visit(Product product, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {
        displayCabinet.vendProduct(product,
                actionable);
    }

    @Override
    public void visit(Misc m, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {
        coinRegister.refundClientMoney(actionable);
    }

    @Override
    public void visit(Coin coin, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {
        coinRegister.depositClientMoney(coin,
                actionable);
    }

    @Override
    public void visit(Maintenance m, List<Product> products, List<Coin> coinsDeposited,
            List<Coin> refundDue, List<Maintenance> maintenances, Actionable<ClientStatus> actionable) {

    }

    public void setCoinRegister(CoinRegister coinRegister) {
        this.coinRegister = coinRegister;
    }

    public void setDisplayCabinet(DisplayCabinet displayCabinet) {
        this.displayCabinet = displayCabinet;
    }


}


