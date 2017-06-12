package steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PageStore {

    WebDriver webDriver;
    List<Object> pages;

    public PageStore() {
        webDriver = new LocalDriver().getApplicationDriver();
        pages = new ArrayList<Object>();
    }

    private boolean runningOnLocal() {
        return System.getProperty("driver").equals("local");
    }


    public <T> T get(Class<T> clazz) {
        for (Object page : pages) {
            if (page.getClass() == clazz)
                return (T) page;
        }
        T page = PageFactory.initElements(webDriver, clazz);
        pages.add(page);
        return page;
    }


    public void destroy() {
        webDriver.quit();
    }

    public WebDriver getDriver() {
        webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        return webDriver;
    }

}
