package org.egov.encryption.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.List;

public class ConvertClass {

    public static <E,P> P convertTo(JsonNode object, Class<E> valueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

        if(object instanceof List)
        {
            ObjectReader reader =
                    objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
            return reader.readValue(object);
        } else {
            return (P)objectMapper.treeToValue(object, valueType);
        }
    }

}
