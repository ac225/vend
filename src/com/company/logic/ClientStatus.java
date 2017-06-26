package com.company.logic;

import com.company.misc.Money;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Works as parameter object that holds the
 */
public class ClientStatus {

	final public List<Maintenance> maintenance;
	final public List<Product> products;
	final public List<Coin> coinsDeposited;
	final public List<Coin> refundDue;
	final public Money totalValueOfDeposit;

	public static ClientStatus initialEmpty() {

		return new ClientStatus(new ArrayList<Product>(), new ArrayList<Coin>(), new ArrayList<Coin>());
	}

	static ClientStatus refund(List<Coin> refundCoins) {

		return new ClientStatus(new ArrayList<Product>(), new ArrayList<Coin>(), new ArrayList<>(refundCoins));
	}

	static ClientStatus productSelected(Product selected) {

		final ArrayList<Product> productsUpdate = new ArrayList<Product>();
		productsUpdate.add(selected);
		return new ClientStatus(productsUpdate, new ArrayList<Coin>(), new ArrayList<Coin>());
	}

	static ClientStatus outOfStock(List<Maintenance> maintenances) {

		return new ClientStatus(maintenances);
	}

	static ClientStatus clientDeposit(List<Coin> deposit) {

		return new ClientStatus(new ArrayList<Product>(), new ArrayList<>(deposit), new ArrayList<Coin>());
	}

	static ClientStatus mergeXX(ClientStatus status, ClientStatus update) {

		ArrayList<Product> productsUpdate = new ArrayList<Product>(status.products);
		productsUpdate.addAll(update.products);

		ArrayList<Coin> depositUpdate = new ArrayList<Coin>(status.coinsDeposited);
		depositUpdate.addAll(update.coinsDeposited);

		ArrayList<Coin> refundUpdate = new ArrayList<Coin>(status.refundDue);
		refundUpdate.addAll(update.refundDue);

		ArrayList<Maintenance> maintainenceUpdate = new ArrayList<Maintenance>(status.maintenance);
		maintainenceUpdate.addAll(update.maintenance);

		return new ClientStatus(productsUpdate, depositUpdate, refundUpdate, maintainenceUpdate);

	}

	public ClientStatus(List<Product> products, List<Coin> coinsDeposited, List<Coin> refundDue) {
		this.products = products;
		this.coinsDeposited = coinsDeposited;
		this.refundDue = refundDue;
		this.maintenance = new ArrayList<>();
		this.totalValueOfDeposit = getTotalValueOfDeposit(coinsDeposited);
	}

	public ClientStatus(List<Product> products, List<Coin> coinsDeposited, List<Coin> refundDue,
			List<Maintenance> maintenances) {
		this.products = products;
		this.coinsDeposited = coinsDeposited;
		this.refundDue = refundDue;
		this.maintenance = maintenances;
		this.totalValueOfDeposit = getTotalValueOfDeposit(coinsDeposited);
	}

	public ClientStatus(List<Maintenance> maintenances) {
		this.products = new ArrayList<>();
		this.coinsDeposited = new ArrayList<>();
		this.refundDue = new ArrayList<>();
		this.maintenance = new ArrayList<>(maintenances);
		this.totalValueOfDeposit = getTotalValueOfDeposit(coinsDeposited);
	}

	private Money getTotalValueOfDeposit(List<Coin> coins) {
		Money totalValue = Money.dollars(BigDecimal.ZERO);

		for (Coin coin : coins) {
			totalValue = totalValue.add(coin.value);
		}

		return totalValue;
	}

}
