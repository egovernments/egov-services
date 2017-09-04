package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class StringExtract extends BasePage {
    HashSet<String> keywords = new HashSet<>();
    private String message = null;

    private WebDriver driver;
    public StringExtract(WebDriver driver) {
        this.driver = driver;
    }

    public String getComplaintNumber(WebElement element) {
        message = element.getText();
        return getOtpFromMessage(message);
    }

    private String getOtpFromMessage(String message) {
        String validMessage = checkNotNull(message, "Message cannot be null").replace(",", "").replace(".", "").replace(":", "");
        TreeMap<Integer, String> sortedMap = new TreeMap<>();
        HashSet<String> defaultKeywords = new HashSet<>();
        defaultKeywords.add("CRN");
        defaultKeywords.add("Request");
        StringTokenizer tokenizer = new StringTokenizer(validMessage.replace(",", "").replace(".", "").replace(":", ""));
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.matches(".*\\d+.*")) {
                for (String defaultKeyword : defaultKeywords) {
                    if (getDistanceBetweenWords(validMessage, defaultKeyword, token) >= 0) {
                        return token;
//                        sortedMap.put(getDistanceBetweenWords(validMessage, defaultKeyword, token), token);

//                        break;
                    }
                }
            }
        }
        if (!sortedMap.isEmpty()) {
            return sortedMap.firstEntry().getValue();
        } else {
            throw new NullPointerException("Given message is invalid.");
        }
    }

    private int getDistanceBetweenWords(String actualString, String firstWord, String secondWord) {
        StringTokenizer st = new StringTokenizer(actualString);
        int numberOfWords = 0;
        boolean start = false;
        if (actualString.contains(firstWord) && actualString.contains(secondWord)) {
            if (actualString.indexOf(firstWord) > actualString.indexOf(secondWord)) {
                firstWord = firstWord + secondWord;
                secondWord = firstWord.substring(0, (firstWord.length() - secondWord.length()));
                firstWord = firstWord.substring(secondWord.length());
            }
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.contains(firstWord)) {
                    start = true;
                    continue;
                }
                if (start) {
                    if (token.contains(secondWord)) {
                        //start = false;
                        break;
                    } else {
                        numberOfWords++;
                    }
                }
            }
            return numberOfWords;
        } else {
            return -1;
        }
    }
}
