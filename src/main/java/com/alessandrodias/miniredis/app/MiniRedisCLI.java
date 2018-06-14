package com.alessandrodias.miniredis.app;

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
        if (!validate(args)){
            System.out.println(errorMessage);
            System.out.println(USAGE);
            exit(ERROR_CODE_BAD_ARGUMENTS);
        }
    }

    private boolean validate(String... args) {
        if (args.length > 0) {
            errorMessage = MSG_TOO_MANY_ARGUMENTS;
            return false;
        }
        return true;
    }

    public void exit(int errorCode) {
        System.exit(errorCode);
    }

    private void startCLI() {

    }
}
