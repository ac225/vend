package com.company.logic;

import com.company.misc.Actionable;
import com.company.misc.EmptyAction;
import com.company.misc.Money;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Product;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class CoinRegisterTest {


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
    public void load_register_with_coins_quantity_A(){
        Money clientAmount = Money.dollars(new BigDecimal(0.00));
        Money bankAmount = Money.dollars(new BigDecimal(14.00));


        run_test_load_money(clientAmount, bankAmount, coinsQuantityA);
    }

    @Test
    public void load_register_with_coins_quantity_B(){
        Money clientAmount = Money.dollars(new BigDecimal(0.00));
        Money bankAmount = Money.dollars(new BigDecimal(10.50));


        run_test_load_money(clientAmount, bankAmount, coinsQuantityB);
    }

    @Test
    public void load_register_with_coins_quantity_C(){
        Money clientAmount = Money.dollars(new BigDecimal(0.00));
        Money bankAmount = Money.dollars(new BigDecimal(11.10));


        run_test_load_money(clientAmount, bankAmount, coinsQuantityC);
    }

    private CoinRegister run_test_load_money(Money clientAmount, Money bankAmount, Map<Coin, Integer> coinsQuantity) {
        CoinRegister a = new CoinRegister(coinsQuantity);
        String message1 = "Client Expected " + clientAmount + " dollars, but was " + a.getTotalDepositedByClient();
        String message2 = "Bank Expected " + bankAmount + " dollars, but was " + a.getTotalAvailable();
        assertTrue(message1, a.getTotalDepositedByClient().compareTo(clientAmount) == 0);
        assertTrue(message2, a.getTotalAvailable().compareTo(bankAmount) == 0);

        return a;
    }

    @Test
    public void client_adds_dime_nickel_quarter_dollar(){

        CoinRegister register = setupCoinRegister();

        Money expected = Money.dollars(BigDecimal.ZERO);

        Coin[] input = {Coin.DIME, Coin.NICKEL, Coin.QUARTER, Coin.DOLLAR};

        addCoinsAndAssert(register, expected, input);
    }

    private void addCoinsAndAssert(CoinRegister register, Money expected, Coin[] input) {
        loopTestDeposit(input, register, expected, new TestAction<Money, Coin>() {
            @Override
            public Money setupExpected(Money expected, Coin coin) {
                return expected.add(coin.value);
            }

            @Override
            public void check(CoinRegister register, Money expected, Coin item) {
                String message1 = "Client amount expected " + expected + " but was " + register.getTotalDepositedByClient();
                assertTrue(message1, register.getTotalDepositedByClient().compareTo(expected) == 0);

            }
        });
    }

    @Test
    public void client_refunded_dime_nickel_quarter_dollar(){

        CoinRegister register = setupCoinRegister();

        Money expected = Money.dollars(BigDecimal.ZERO);

        Coin[] input = {Coin.DIME, Coin.NICKEL, Coin.QUARTER, Coin.DOLLAR};

        addCoinsAndAssert(register, expected, input);

        List<Coin> refund = register.refundClientMoney(new EmptyAction<>());
        assertEquals(refund, (Arrays.asList(input)));
    }

    /**
     * Bank   = 11.00 USD
     * Client =  1.40 USD
     * A      =   .65 USD
     * Refund =   .75 (3 x Quarters)
     */
    @Test
    public void client_adds_dime_nickel_quarter_dollar_buys_product_A_gets_change(){

        HashMap<Coin, Integer> expectedChange = new HashMap<>();
        expectedChange.put(Coin.QUARTER, 3);

        purchaseAndVerifyChange(new Coin[]{Coin.DIME, Coin.NICKEL, Coin.QUARTER, Coin.DOLLAR}, Product.A, expectedChange);

    }

    /**
     * Bank   = 11.00 USD
     * Client =  1.00 USD
     * B      =  1.00 USD
     * Refund =    .0
     */
    @Test
    public void client_adds_4_quarters_buys_product_B_no_change(){

        HashMap<Coin, Integer> expectedChange = new HashMap<>();

        purchaseAndVerifyChange(new Coin[]{Coin.QUARTER, Coin.QUARTER, Coin.QUARTER, Coin.QUARTER}, Product.B, expectedChange);

    }

    /**
     * Bank   = 11.00 USD
     * Client =  1.00 USD
     * A      =  1.00 USD
     * Refund =   .35 USD
     */
    @Test
    public void client_adds_dollar_product_A_gets_change(){

        HashMap<Coin, Integer> expectedChange = new HashMap<>();
        expectedChange.put(Coin.QUARTER, 1);
        expectedChange.put(Coin.DIME, 1);

        purchaseAndVerifyChange(new Coin[]{Coin.DOLLAR}, Product.A, expectedChange);

    }

    /**
     * Bank   = 11.00 USD
     * Client =  1.00 USD
     * A      =  1.00 USD
     * Refund =   .35 USD
     */
    @Test
    public void client_adds_2_quarters_and_cancels(){

        List<Coin> expectedChange = new ArrayList<>();
        expectedChange.add(Coin.QUARTER);
        expectedChange.add(Coin.QUARTER);

        addCoinsAndCancelAndRefunds(new Coin[]{Coin.QUARTER, Coin.QUARTER}, Product.A, expectedChange);

    }

    private void purchaseAndVerifyChange(Coin[] input, Product purchase, HashMap<Coin, Integer> expectedChange) {
        CoinRegister register = setupCoinRegister();

        Money expected = Money.dollars(BigDecimal.ZERO);

        addCoinsAndAssert(register, expected, input);

        Map<Coin, Integer> change = register.completeTransaction(purchase);
        String message = "Incorrect change after purchasing " + purchase +
                ". Change " + change + " Expected " + expectedChange;
        assertEquals(message, change, expectedChange);
    }

    private void addCoinsAndCancelAndRefunds(Coin[] input, Product purchase, List<Coin> expectedRefund) {
        CoinRegister register = setupCoinRegister();

        Money expected = Money.dollars(BigDecimal.ZERO);

        addCoinsAndAssert(register, expected, input);

        List<Coin> change = register.refundClientMoney(new EmptyAction<>());
        String message = "Incorrect change after cancel/coin return " + purchase +
                ". Change " + change + " Expected " + expectedRefund;
        assertEquals(message, change, expectedRefund);
    }


    private void loopTestDeposit(Coin[] input, CoinRegister register, Money expected, TestAction<Money, Coin> testAction) {

        for(Coin coin: input) {
            expected = testAction.setupExpected(expected, coin);

            register.depositClientMoney(coin, new Actionable<ClientStatus>() {
                @Override
                public void onCompleted(ClientStatus clientStatus) {
                    assertEquals(clientStatus.coinsDeposited.get(0), coin);
                }

                @Override
                public void onError(ClientStatus clientStatus, Throwable e) {
                    Assert.fail("Should have called onError() method");
                }
            });

            testAction.check(register, expected, coin);
        }
    }




    private CoinRegister setupCoinRegister() {
        Money clientAmount = Money.dollars(new BigDecimal(0.00));
        Money bankAmount = Money.dollars(new BigDecimal(11.10));

        return run_test_load_money(clientAmount, bankAmount, coinsQuantityC);
    }

    interface TestAction<T, K> {
        Money setupExpected(Money money, K item);
        void check(CoinRegister register, T expected, K item);
    }



}