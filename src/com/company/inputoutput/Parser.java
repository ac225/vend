package com.company.inputoutput;

import com.company.tokens.enums.Tokenable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Parse each line of input and convert to enums
 */
public class Parser {

    Tokenizer tokenizer;

    final public String delimiter = "\\s*,\\s*";

    public Parser(){

    }

    public List<Tokenable> parse(String input){

        try ( Scanner scanner = new Scanner(input)){
        	scanner.useDelimiter(delimiter);
        	return process(scanner);
        }
    }

    private List<Tokenable> process(Scanner scanner) {
        List<Tokenable> commands = new ArrayList<>();

        while(scanner.hasNext()){

            String s = scanner.next();
            Tokenable token = tokenizer.convertToToken(s);
            commands.add(token);
        }

        return commands;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }


}
