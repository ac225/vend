# vend

## Assumptions

* Single Threaded
* Outputs SERVICE when an item selected purchase is not available and coins are returned
* Calculates the change from the available quantity of coins in the machine.  That is, it will use smaller coins to make up the shortfall when bigger coins are not available (e.g 4 Quarters will be substituted for 1 Dollar)
* Specification did not specify remedial steps to take when there is not enough change in the machine. This edge case has not been programmatically handled.

## Requires
* Junit to run the steps

## How it works in a nutshell

VendingFacade.java sets up the wiring for most of the classes and is the entry point in the application after Main.java.

CoinRegistry.java holds the record of the available coins and change in the machine, as well as the amount inserted by the client
DisplayCabinet.java holds the record of the available products

Scanner breaks up the each token and converts these into enums (Parser.java and Tokenizer.java)
enums have actions associated with them that determine whether CoinRegistry.java or DisplayCabinet.java should act upon them (dispatched by the CommandProcessor.java).

1. CoinRegistry.java will add the value of inserted coins - or refund these coins when it encounters a coin-return.
2. DisplayCabinet.java vends the product if it is available and there is enough money deposited by the client
3. There are some prevendchecks in VendingFacade.java before it displays the output

## Tests

There are unit tests and integration tests in the test folder. The integration test is an end to end test that simulates user input (insert coin, coin-return and product selection) and verifies vending machine output (refund, change and product vend).


