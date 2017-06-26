package com.company.logic;


import com.company.misc.Actionable;
import com.company.misc.Money;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Product;

import java.math.BigDecimal;
import java.util.*;

/**
 * Logic for money related features
 * Holds a count of the coins in the system:
 * - Coins held in the vending machine
 * - Coins deposited by the user, etc
 *
 * Also, provides method to determine change due
 */
public class CoinRegister {

    final private Map<Coin, Integer> coinsAvailable;
    private Money totalAvailable = Money.dollars(BigDecimal.ZERO);

    private Money totalDepositedByClient = Money.dollars(BigDecimal.ZERO);
    private List<Coin> coinsDepositedByClient = new ArrayList<>();


    /**
     * Constructor to instantiate with a map of coins and their quantity
     *
     * @param coinsAvailable number of coins in the machine
     */
    public CoinRegister(Map<Coin, Integer> coinsAvailable) {

        this.coinsAvailable = new HashMap<>(coinsAvailable);

        updateTotalValue();
    }

    Money getTotalAvailable() {
        return totalAvailable;
    }

    private void updateTotalValue() {
        Money totalValue = Money.dollars(BigDecimal.ZERO);

        for (Coin coin: coinsAvailable.keySet()) {
            BigDecimal quantityOfCoins = new BigDecimal(coinsAvailable.get(coin));
            BigDecimal valueOfCoins = coin.value.getAmount().multiply(quantityOfCoins);

            totalValue = totalValue.add(Money.dollars(valueOfCoins));
        }

        totalAvailable = totalValue;
    }

    public Money getTotalDepositedByClient(){
    	return totalDepositedByClient;
    }

    private void addCoin(Coin coin) {

        Integer quantity = coinsAvailable.get(coin);
        Integer quantityUpdated = quantity.intValue() + 1;
        coinsAvailable.put(coin, quantityUpdated);

        updateTotalValue();
    }

    boolean canAfford(Money price){
    	return totalDepositedByClient.compareTo(price) >= 0;
    }
    
    public Map<Coin, Integer> completeTransaction(Product product){

        Money changeDue = getChangeDue(product);
        Money changeRemaining = Money.dollars(changeDue.getAmount());

        for(Coin coin: coinsDepositedByClient){
            addCoin(coin);
        }
        coinsDepositedByClient.clear();
        totalDepositedByClient = Money.dollars(BigDecimal.ZERO);

        return getChangeToReturn(changeRemaining);
    }

	private Map<Coin, Integer> getChangeToReturn(Money changeRemaining) {
		Map<Coin, Integer> coinsToReturn = new LinkedHashMap<>();

        for(Coin coin: Coin.reversedSortedByCoinValue()) {
            if (coin.value.compareTo(changeRemaining) > 0){
                continue;
            }
            Integer quantity = coinsAvailable.get(coin);
            if (quantity > 0) {
                int coinReturnQuantity = changeRemaining.divide(coin.value).getAmount().intValue();
                if (coinReturnQuantity > quantity) {
                    coinReturnQuantity = quantity;
                }
                Money coinValue = coin.value.multiply(Money.dollars(BigDecimal.valueOf(coinReturnQuantity)));
                totalAvailable = totalAvailable.subtract(coinValue);

                changeRemaining = changeRemaining.subtract(coinValue);

                coinsToReturn.put(coin, coinReturnQuantity);

            }
        }

        return coinsToReturn;
	}

    private Money getChangeDue(Product product) {
        return totalDepositedByClient.subtract(product.price);

    }

    public void depositClientMoney(Coin clientDeposit,
                                   Actionable<ClientStatus> actionable) {

        coinsDepositedByClient.add(clientDeposit);
        totalDepositedByClient = totalDepositedByClient.add(clientDeposit.value);

        List<Coin> coinsDeposit = new ArrayList<>();
        coinsDeposit.add(clientDeposit);
        
        final ClientStatus update = ClientStatus.clientDeposit(coinsDeposit);

        actionable.onCompleted(update);

    }

    public List<Coin> refundClientMoney(Actionable<ClientStatus> actionable) {

        List<Coin> refund = new ArrayList<>(coinsDepositedByClient);

        coinsDepositedByClient.clear();
        totalDepositedByClient = Money.dollars(BigDecimal.ZERO);

        actionable.onCompleted(ClientStatus.refund(refund));
        return refund;
    }


}


