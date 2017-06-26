package com.company;

import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Product;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private Map<Product, Integer> productQuantities = new HashMap<>();
    {
        productQuantities.put(Product.A, 10);
        productQuantities.put(Product.B, 10);
        productQuantities.put(Product.C, 10);
    }

    private Map<Coin, Integer> coinsQuantity = new HashMap<>();
    {
        coinsQuantity.put(Coin.NICKEL, 10);
        coinsQuantity.put(Coin.DIME, 10);
        coinsQuantity.put(Coin.DOLLAR, 10);
        coinsQuantity.put(Coin.QUARTER, 10);
    }

    private void run() {

        VendingFacade vendingFacade = new VendingFacade(productQuantities, coinsQuantity);
        vendingFacade.run();

        System.out.printf("Type EXIT to stop\n\n");

    }

    public static void main(String[] args) {

        new Main().run();

    }

}

