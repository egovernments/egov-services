package utils;

public class ResourceHelper {

    public static String getBaseURI() {

        String baseUrl = null;

        switch (System.getProperty("env")){

            case "dev" :

                baseUrl = "http://egov-micro-dev.egovernments.org";
                break;

            case "qa" :

                baseUrl = "http://egov-micro-qa.egovernments.org";
                break;

            case "pilot" :

                baseUrl = "http://kurnool-pilot-services.egovernments.org";
                break;

            default :

                baseUrl = "http://kurnool-pilot-services.egovernments.org";
                break;
        }

        return baseUrl;
    }
}
