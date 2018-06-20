package com.alessandrodias.miniredis.app;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class MiniRedisCLITest {

    private static final String EOL =
            System.getProperty("line.separator");

    private ByteArrayOutputStream consoleText;
    private PrintStream console;
    private MiniRedisCLI miniRedisCLI;
    private int errorCode;

    @Before
    public void setUp() {
        consoleText = new ByteArrayOutputStream();
        console = System.out;
        System.setOut(new PrintStream(consoleText));
        miniRedisCLI = new MiniRedisCLI();
    }

//    @Test
//    public void testShouldShowUsageWhenAnyNumberOfArgumentsIsSupplied() {
//        miniRedisCLI.execute("a", "b", "c");
//        assertAbort(MiniRedisCLI.MSG_TOO_MANY_ARGUMENTS);
//    }
//
//    private void assertAbort(String expectedMessage) {
//        assertEquals(expectedMessage + EOL + MiniRedisCLI.USAGE + EOL,
//                consoleText.toString());
//        assertEquals(MiniRedisCLI.ERROR_CODE_BAD_ARGUMENTS, errorCode);
//    }
}
