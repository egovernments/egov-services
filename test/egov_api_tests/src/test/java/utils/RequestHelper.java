package utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class RequestHelper {
    public static Map asMap(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        Map map = objectMapper.convertValue(o, HashMap.class);
        return map;
    }

    public static String getJsonString(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        try {
            return objectMapper.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getXMLString(Object o) {
        try {
            JAXBContext context = JAXBContext.newInstance(o.getClass());

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter stringWriter = new StringWriter();

            m.marshal(o, stringWriter);
            String xmlString = stringWriter.toString();
            return xmlString.substring(xmlString.indexOf('\n') + 1);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }
}
