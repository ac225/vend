package com.company;

import com.company.inputoutput.Parser;
import com.company.inputoutput.ToStringHelper;
import com.company.inputoutput.Tokenizer;
import com.company.logic.ClientStatus;
import com.company.logic.CoinRegister;
import com.company.logic.CommandProcessor;
import com.company.logic.DisplayCabinet;
import com.company.misc.EmptyAction;
import com.company.misc.Visitor;
import com.company.tokens.enums.Coin;
import com.company.tokens.enums.Product;
import com.company.tokens.enums.Tokenable;

import java.util.*;

/**
 * Initial Entry to the application, takes input, process inputs into tokens (commands)
 * and dispatches to the processors.  Takes the output from them, and displays output
 *
 */
class VendingFacade {

    private Map<Product, Integer> productQuantities;
    private Map<Coin, Integer> coinsQuantity;

    /**
     * Loads up coins and products with a set number of products
     * and coins
     */
    VendingFacade() {
        productQuantities = new HashMap<>();
        productQuantities.put(Product.A, 1);
        productQuantities.put(Product.B, 1);
        productQuantities.put(Product.C, 1);

        coinsQuantity = new HashMap<>();
        coinsQuantity.put(Coin.NICKEL, 10);
        coinsQuantity.put(Coin.DIME, 10);
        coinsQuantity.put(Coin.DOLLAR, 10);
        coinsQuantity.put(Coin.QUARTER, 10);

        setupService();
    }

    /**
     * Loads up coins and products with the values provided in the parameters
     * @param productQuantities products and their respective quantities
     * @param coinsQuantity the number of each different coin
     */
    VendingFacade(Map<Product, Integer> productQuantities, Map<Coin, Integer> coinsQuantity) {
        this.productQuantities = new HashMap<>(productQuantities);
        this.coinsQuantity = new HashMap<>(coinsQuantity);
        setupService();
    }

    void run() {
        System.out.printf("Type EXIT to stop\n\n");

        processInput();
    }

    private void processInput() {

    	try (Scanner scanner = new Scanner(System.in)){

    		System.out.printf("> ");
    		while (scanner.hasNextLine()) {
    			if (processLine(scanner)) break;
    			System.out.printf("> ");
    		}
    	}

    }

    private boolean processLine(Scanner scanner) {
        String line = scanner.nextLine();

        if (line.equalsIgnoreCase("EXIT")) return true;

        String output = processInput(line);
        print(output);

        return false;
    }

    String processInput(String line) {
        List<Tokenable> commands = parser.parse(line);
        ClientStatus newStatus = ClientStatus.initialEmpty();
        ClientStatus status = commandProcessor.processCommands(newStatus, commands);
        List<Tokenable> output = doPreVendChecks(status);
        return prettyOutput(output);
    }

    /**
     * Last checks that determine things like change due, whether product is available
     * @param status Parameter object
     * @return
     */
    private List<Tokenable> doPreVendChecks(ClientStatus status) {

        List<Tokenable> output = new ArrayList<>();
        
        output.addAll(status.maintenance);

        if (!status.maintenance.isEmpty()) {
            refund(output);
        } else {
            output.addAll(status.refundDue);
        }

        if (!status.products.isEmpty()) {
            Product product = status.products.get(0);
            if (insufficientFunds(product)) {
                refund(output);
            }
            else {
            	output.addAll(status.products);
            	output.addAll(getChange(product));
            }
        }
        return output;
    }

	private void refund(List<Tokenable> output) {
		List<Coin> refundedClientMoney = coinRegister.refundClientMoney(new EmptyAction<>());
		output.addAll(refundedClientMoney);
	}

	private boolean insufficientFunds(Product product) {
		return product.price.compareTo(coinRegister.getTotalDepositedByClient()) > 0;
	}

	private String prettyOutput(List<?> output){
        return toStringHelper.toString(output);
    }

    private void print(String output) {
        System.out.printf("%s\n\n", output);
    }

    private List<Coin> getChangeAsList(Map<Coin, Integer> map) {

        List<Coin> coins = new LinkedList<>();

        for (Map.Entry<Coin, Integer> coinAndQuantity : map.entrySet()) {
            Integer quantity = coinAndQuantity.getValue();
            Coin coin = coinAndQuantity.getKey();
            for (int i = 0; i < quantity; i++) {
                coins.add(coin);
            }
        }
        return coins;
    }

    private List<Coin> getChange(Product product) {
        Map<Coin, Integer> coinsMap = coinRegister.completeTransaction(product);
        return getChangeAsList(coinsMap);
    }


    private void setupService() {

        Tokenizer tokenizer = new Tokenizer();
        parser = new Parser();
        parser.setTokenizer(tokenizer);


        DisplayCabinet displayCabinet = new DisplayCabinet(productQuantities);

        coinRegister = new CoinRegister(coinsQuantity);
        Visitor visitor = new Visitor();
        visitor.setCoinRegister(coinRegister);
        visitor.setDisplayCabinet(displayCabinet);
        displayCabinet.setCoinRegister(coinRegister);
        
        commandProcessor = new CommandProcessor();
        this.commandProcessor.setVisitor(visitor);

        toStringHelper = new ToStringHelper(", ");
    }

    private Parser parser;
    private CoinRegister coinRegister;
    private ToStringHelper toStringHelper;
    private CommandProcessor commandProcessor;

}
