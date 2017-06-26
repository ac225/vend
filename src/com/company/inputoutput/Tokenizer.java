package com.company.inputoutput;


import com.company.tokens.enums.*;

/**
 * Takes a string and converts to enum
 */
public class Tokenizer {


    public Tokenable convertToToken(String command) {
        return lookup(command);
    }


    public Tokenable lookup(String command) {

        for (Maintenance e : Maintenance.values()) {
            if (e.commandWord.equalsIgnoreCase(command))
                return e;
        }

        for (Product e : Product.values()) {
            if (e.commandWord.equalsIgnoreCase(command))
                return e;
        }

        for (Coin e : Coin.values()) {
            if (e.commandWord.equalsIgnoreCase(command))
                return e;
        }

        for (Misc e : Misc.values()) {
            if (e.commandWord.equalsIgnoreCase(command))
                return e;
        }

        throw new RuntimeException("Sorry, I didn't recognise your input " + command);
    }


}