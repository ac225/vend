package com.company;

import com.company.misc.Actionable;
import com.company.misc.EmptyAction;
import com.company.misc.Money;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Product;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    private String inputA = "N, D, Q, DOLLAR, GET-A";
    private String expectedA = "A, Q, Q, Q";

    private String inputB = "N, D, Q, DOLLAR, GET-B";
    private String expectedB = "B, Q, D, N";

    private String inputC = "N, D, D, Q, DOLLAR, GET-C";
    private String expectedC = "C";

    private String inputD = "Q, Q, Q, Q, GET-B";
    private String expectedD = "B";

    private String inputE = "Q, Q, COIN-RETURN";
    private String expectedE = "Q, Q";

    private String inputF = "DOLLAR, GET-A";
    private String expectedF = "A, Q, D";

    private String inputG = "N, D, Q, DOLLAR, COIN-RETURN";
    private String expectedG = "N, D, Q, DOLLAR";


    private Map<Coin, Integer> coinsQuantityA = new HashMap<>();
    {
        coinsQuantityA.put(Coin.NICKEL, 10);  // 10 * 0.05 =  0.50
        coinsQuantityA.put(Coin.DIME, 10);    // 10 * 0.10 =  1,00
        coinsQuantityA.put(Coin.DOLLAR, 10);  // 10 * 1    = 10.00
        coinsQuantityA.put(Coin.QUARTER, 10); // 10 * 0.25 = 02.50
    }                                             // Total     = 14

    private Map<Coin, Integer> coinsQuantityB = new HashMap<>();
    {
        coinsQuantityB.put(Coin.NICKEL, 3);   //  3 * 0.05 =  0.15
        coinsQuantityB.put(Coin.DIME, 1);     //  1 * 0.10 =  0.10
        coinsQuantityB.put(Coin.DOLLAR, 7);   //  7 * 1    =  7.00
        coinsQuantityB.put(Coin.QUARTER, 13); // 13 * 0.25 =  3.25
        // Total     = 10.50
    }

    private Map<Coin, Integer> coinsQuantityC = new HashMap<>();
    {
        coinsQuantityC.put(Coin.NICKEL, 31);
        coinsQuantityC.put(Coin.DIME, 73);
        coinsQuantityC.put(Coin.DOLLAR, 1);
        coinsQuantityC.put(Coin.QUARTER, 5);
    }

    @Test
    public void run_example_a(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputA);

        assertEquals(expectedA, output);
    }

    @Test
    public void run_example_b(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputB);

        assertEquals(expectedB, output);
    }


    @Test
    public void run_example_c(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputC);

        assertEquals(expectedC, output);
    }

    @Test
    public void run_example_d(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputD);

        assertEquals(expectedD, output);
    }

    @Test
    public void run_example_e(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputE);

        assertEquals(expectedE, output);
    }

    @Test
    public void run_example_f(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputF);

        assertEquals(expectedF, output);
    }

    @Test
    public void run_example_g(){

        VendingFacade vendingFacade = new VendingFacade();
        String output = vendingFacade.processInput(inputG);

        assertEquals(expectedG, output);
    }

}