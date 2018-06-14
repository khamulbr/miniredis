package com.alessandrodias.miniredis.app;

import com.alessandrodias.miniredis.model.MiniRedisCommand;
import com.alessandrodias.miniredis.model.MiniRedisInstruction;

import java.util.Scanner;

public class MiniRedisCLI {

    static final String USAGE = "Usage:java MiniRedisCLI";
    static final String MSG_TOO_MANY_ARGUMENTS  = "too many arguments";
    static final int ERROR_CODE_BAD_ARGUMENTS   = 1;

    private String errorMessage;

    public static void main(String... args) {
        MiniRedisCLI miniRedisCLI = new MiniRedisCLI();
        miniRedisCLI.execute(args);
    }

    public void execute(String... args) {
        if (!validateArgumentsReceived(args)){
            System.out.println(errorMessage);
            System.out.println(USAGE);
            exit(ERROR_CODE_BAD_ARGUMENTS);
        }
        loop();
    }

    private boolean validateArgumentsReceived(String... args) {
        if (args.length > 0) {
            errorMessage = MSG_TOO_MANY_ARGUMENTS;
            return false;
        }
        return true;
    }

    public void exit(int errorCode) {
        System.exit(errorCode);
    }

    private void loop() {
        sayHello();
        Scanner keyboard = new Scanner(System.in);
        String commandParsed;
        do {
            System.out.print("> ");
            String instructionReceived = keyboard.next();
            commandParsed = parseInstruction(instructionReceived).getCommand();
        } while (!(commandParsed.toUpperCase()).equals(MiniRedisCommand.EXIT.getCommand()));
        sayGoodbye();
    }

    private MiniRedisInstruction parseInstruction(String instructionReceived) {
        MiniRedisInstruction miniRedisInstruction = new MiniRedisInstruction();
        return miniRedisInstruction.parse(instructionReceived);
    }

    private void sayGoodbye() {
        System.out.println("Thank you for using MINIREDIS VERSION 1.0");
    }

    private void sayHello() {
        System.out.println("MINIREDIS VERSION 1.0");
        System.out.println("Please type a REDIS command, HELP to see instructions and command list, or EXIT to leave right away");
    }
}
