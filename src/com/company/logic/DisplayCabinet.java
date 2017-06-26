package com.company.logic;

import com.company.misc.Actionable;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Product;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Logic to handle and hold products
 */
public class DisplayCabinet {

	final private Map<Product, Integer> displayProducts;
	private CoinRegister coinRegister;

	
	public DisplayCabinet(Map<Product, Integer> displayProducts) {

		this.displayProducts = new HashMap<>(displayProducts);
	}

	boolean hasProduct(Product product) {
		return displayProducts.get(product) > 0;
	}

	public void vendProduct(Product product, Actionable<ClientStatus> action) {

		if (hasProduct(product)) {

			final ClientStatus update = ClientStatus.productSelected(product);

			if (coinRegister.canAfford(product.price)) {
				Integer quantity = displayProducts.get(product) - 1;

				displayProducts.put(product, quantity);
				action.onCompleted(update);

				return;
			} else {
				action.onError(update, new Throwable("Sorry, " + product + " is " + product.price
						+ " and you have not inserted enough coins.\n Your money have been refunded."));
				return;
			}
		}
		
		List<Maintenance> maintenanceUpdate = new ArrayList<>();
		maintenanceUpdate.add(Maintenance.SERVICE);
		final ClientStatus update = ClientStatus.outOfStock(maintenanceUpdate);
		
		action.onError(update, new Throwable("Sorry, " + product + " is currently not in stock"));
	}

	public void setCoinRegister(CoinRegister coinRegister) {
		this.coinRegister = coinRegister;
	}
	

	
}
