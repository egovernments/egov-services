package pages;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.FileExtension;
import utils.FileFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConstructElement extends BasePage {

    private WebDriver driver;
    private JSONObject jsonObject;

    public ConstructElement(WebDriver driver) {
        this.driver = driver;
    }

    private int findElementPositionInJSON(String screen, String element, String value) throws IOException {

        FileFinder p = FileFinder.fileFinder("src/test/resources/elements/");
        File f = p.find(screen, FileExtension.ELEMENTS);
        BufferedReader in = new BufferedReader(new FileReader(f));
        String str;
        StringBuilder json = new StringBuilder();

        while ((str = in.readLine()) != null) {
            json.append(str);
        }
        in.close();

        jsonObject = new JSONObject(json.toString());
        int pos = 0;

        for (int i = 0; i < jsonObject.getJSONArray("elements").length(); i++) {
            if ((jsonObject.getJSONArray("elements").getJSONObject(pos).getString("elementName").equals(element)))
                break;
            else pos++;
        }

        if (jsonObject.getJSONArray("elements").getJSONObject(pos).getString("value").contains("%s")) {
            String locator = jsonObject.getJSONArray("elements").getJSONObject(pos).getString("value");
            jsonObject.getJSONArray("elements").getJSONObject(pos).put("value", locator.replace("%s", value));
        }

        return pos;
    }

    public WebElement buildElement(String screen, String element, String value) throws IOException {

        int pos = findElementPositionInJSON(screen, element, value);
        String str = jsonObject.getJSONArray("elements").getJSONObject(pos).getString("value");
        WebElement webElement = null;

        switch (jsonObject.getJSONArray("elements").getJSONObject(pos).getString("identifier")) {

            case "id":
                waitForElementsSizeIsGreaterThan0((By.id(str)), driver);
                webElement = driver.findElement(By.id(str));
                break;

            case "css":
                waitForElementsSizeIsGreaterThan0((By.cssSelector(str)), driver);
                webElement = driver.findElement(By.cssSelector(str));
                break;

            case "xpath":
                waitForElementsSizeIsGreaterThan0((By.xpath(str)), driver);
                webElement = driver.findElement(By.xpath(str));
                break;

            case "linkText":
                waitForElementsSizeIsGreaterThan0((By.linkText(str)), driver);
                webElement = driver.findElement(By.linkText(str));
                break;

            case "className":
                waitForElementsSizeIsGreaterThan0((By.className(str)), driver);
                webElement = driver.findElement(By.className(str));
                break;

            case "name":
                waitForElementsSizeIsGreaterThan0((By.name(str)), driver);
                webElement = driver.findElement(By.name(str));
                break;

            case "tagName":
                waitForElementsSizeIsGreaterThan0((By.tagName(str)), driver);
                webElement = driver.findElement(By.tagName(str));
                break;

            case "partialLinkText":
                waitForElementsSizeIsGreaterThan0((By.partialLinkText(str)), driver);
                webElement = driver.findElement(By.partialLinkText(str));
                break;
        }
        return webElement;
    }
}
