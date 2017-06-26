package com.company.logic;

import com.company.misc.Actionable;
import com.company.misc.Money;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Product;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DisplayCabinetTest {

    private Map<Product, Integer> productQuantityStocked = new HashMap<>();
    {
        productQuantityStocked.put(Product.A, 1);
        productQuantityStocked.put(Product.B, 1);
        productQuantityStocked.put(Product.C, 1);
    }

    private Map<Product, Integer> productQuantitySome = new HashMap<>();
    {
        productQuantitySome.put(Product.A, 0);
        productQuantitySome.put(Product.B, 1);
        productQuantitySome.put(Product.C, 3);
    }

    private Map<Product, Integer> productQuantityEmpty = new HashMap<>();
    {
        productQuantityEmpty.put(Product.A, 0);
        productQuantityEmpty.put(Product.B, 0);
        productQuantityEmpty.put(Product.C, 0);
    }

    @Test
    public DisplayCabinet load_cabinet_with_empty(){

        return run_test_load_product(productQuantityEmpty, new boolean []{false, false, false});
    }

    @Test
    public DisplayCabinet load_cabinet_with_stocked(){

        return run_test_load_product(productQuantityStocked, new boolean []{true, true, true});

    }

    @Test
    public DisplayCabinet load_cabinet_with_2(){

        return run_test_load_product(productQuantitySome, new boolean []{false, true, true});
    }

    @Test
    public void buy_product_but_not_available(){

        DisplayCabinet displayCabinet = load_cabinet_with_empty();

        for(Product purchase: new Product[] {Product.A, Product.B, Product.C}) {

            displayCabinet.setCoinRegister(new InsufficientCashRegister(new HashMap<>()));
            displayCabinet.vendProduct(purchase, new Actionable<ClientStatus>() {
                @Override
                public void onCompleted(ClientStatus clientStatus) {
                    fail("Should not have called complete when product is not available");
                }

                @Override
                public void onError(ClientStatus clientStatus, Throwable e) {
                    assertEquals(Collections.EMPTY_LIST, clientStatus.products);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.coinsDeposited);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.refundDue);
                    assertEquals(Collections.singletonList(Maintenance.SERVICE), clientStatus.maintenance);
                }
            });
        }
    }

    @Test
    public void buy_product_but_not_enough_cash(){

        DisplayCabinet displayCabinet = load_cabinet_with_stocked();

        for(Product purchase: new Product[] {Product.A, Product.B, Product.C}) {

            displayCabinet.setCoinRegister(new InsufficientCashRegister(new HashMap<>()));
            displayCabinet.vendProduct(purchase, new Actionable<ClientStatus>() {
                @Override
                public void onCompleted(ClientStatus clientStatus) {
                    fail("Should not have called complete when there is not enough cash");
                }

                @Override
                public void onError(ClientStatus clientStatus, Throwable e) {
                    assertEquals(Collections.singletonList(purchase), clientStatus.products);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.coinsDeposited);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.refundDue);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.maintenance);
                }
            });
        }
    }

    @Test
    public void buy_product_and_enough_cash(){

        DisplayCabinet displayCabinet = load_cabinet_with_stocked();

        for(Product purchase: new Product[] {Product.A, Product.B, Product.C}) {

            displayCabinet.setCoinRegister(new EnoughCashRegister(new HashMap<>()));
            displayCabinet.vendProduct(purchase, new Actionable<ClientStatus>() {
                @Override
                public void onCompleted(ClientStatus clientStatus) {
                    assertEquals(Collections.singletonList(purchase), clientStatus.products);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.coinsDeposited);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.refundDue);
                    assertEquals(Collections.EMPTY_LIST, clientStatus.maintenance);
                }

                @Override
                public void onError(ClientStatus clientStatus, Throwable e) {
                    fail("Should not have called complete when there is enough cash");
                }
            });
        }
    }

    private DisplayCabinet run_test_load_product(Map<Product, Integer> products, boolean[] expected) {

        DisplayCabinet a = new DisplayCabinet(products);
        assertEquals(a.hasProduct(Product.A), expected[0]);
        assertEquals(a.hasProduct(Product.B), expected[1]);
        assertEquals(a.hasProduct(Product.C), expected[2]);

        return a;
    }


    private CoinRegister run_test_load_product(Money clientAmount, Money bankAmount, Map<Coin, Integer> coinsQuantity) {

        CoinRegister a = new CoinRegister(coinsQuantity);
        String message1 = "X Client Expected " + clientAmount + " dollars, but was " + a.getTotalDepositedByClient();
        String message2 = "Bank Expected " + bankAmount + " dollars, but was " + a.getTotalAvailable();
        assertTrue(message1, a.getTotalDepositedByClient().compareTo(clientAmount) == 0);
        assertTrue(message2, a.getTotalAvailable().compareTo(bankAmount) == 0);

        return a;
    }

    class InsufficientCashRegister extends CoinRegister {

        public InsufficientCashRegister(Map<Coin, Integer> coinsAvailable) {
            super(coinsAvailable);
        }

        @Override
        boolean canAfford(Money price){
            return false;
        }

    }

    class EnoughCashRegister extends CoinRegister {

        public EnoughCashRegister(Map<Coin, Integer> coinsAvailable) {
            super(coinsAvailable);
        }

        @Override
        boolean canAfford(Money price){
            return true;
        }

    }


}