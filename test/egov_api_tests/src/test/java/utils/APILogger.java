package utils;

import org.testng.Reporter;

public class APILogger {
    public void log(String s) {
        Reporter.log(s, true);
    }
}
