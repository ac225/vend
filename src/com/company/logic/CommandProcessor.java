package com.company.logic;

import java.util.ArrayList;
import java.util.List;

import com.company.misc.Actionable;
import com.company.misc.Visitible;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Maintenance;
import com.company.tokens.enums.Product;
import com.company.tokens.enums.Tokenable;

public class CommandProcessor {

	
	
	public CommandProcessor() {
		products = new ArrayList<>();
		coinsDeposited = new ArrayList<>();
		refundDue = new ArrayList<>();
		maintenances = new ArrayList<>();
	}


	public ClientStatus processCommands(ClientStatus status, List<Tokenable> commands) {

		resetStatusArrays();
		
		for (Tokenable command : commands) {

            command.accept(visitor, products, coinsDeposited, refundDue, maintenances, 
            		new Actionable<ClientStatus>() {
                @Override
                public void onCompleted(ClientStatus update) {
                	update(update);
                }

                @Override
                public void onError(ClientStatus update, Throwable e) {
                	update(update);
                }
            });

        }

        final ClientStatus update = new ClientStatus(products, coinsDeposited, refundDue, maintenances);
        return update;
    }


	private void resetStatusArrays() {
		products.clear();
		coinsDeposited.clear();
		refundDue.clear();
		maintenances.clear();
	}

	
    private void update(ClientStatus update) {
    	products.addAll(update.products);
        coinsDeposited.addAll(update.coinsDeposited);
        refundDue.addAll(update.refundDue);
        maintenances.addAll(update.maintenance);
    }


	private final List<Product> products;
    private Visitible visitor;
	private List<Coin> coinsDeposited;
	private final List<Coin> refundDue;
	private final List<Maintenance> maintenances;

    public void setVisitor(Visitible visitor) {
        this.visitor = visitor;
    }
}
