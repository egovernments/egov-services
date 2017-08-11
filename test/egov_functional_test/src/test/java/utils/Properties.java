package utils;

public class Properties {

    public static final long waitTime = 30;
    private static final PropertiesReader propertiesReader = new PropertiesReader();
    public static final String url = propertiesReader.getUrl();

}
