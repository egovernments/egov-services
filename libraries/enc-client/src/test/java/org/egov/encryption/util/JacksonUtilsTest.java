package org.egov.encryption.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
public class JacksonUtilsTest {

    ObjectMapper mapper;

    @Before
    public void initializeCommonObjects() {
        JsonFactory jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void superimposeEncryptedDataOnOriginalNodeTest() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        JsonNode encryptedNode = mapper.readTree("{\"User\":{\"userName\":\"123|jkafsdhkjhalfsj\",\"name\":\"123|" +
                "jkafsdhkjhalfsj\",\"mobileNumber\":\"123|hdskjahkjfk\"}}");

        JsonNode outputNode = JacksonUtils.merge(encryptedNode, originalNode);

        JsonNode expectedNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"123|jkafsdhkjhalfsj\"," +
                "\"name\":\"123|jkafsdhkjhalfsj\",\"gender\":\"male\",\"mobileNumber\":\"123|hdskjahkjfk\"," +
                "\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        assertEquals(expectedNode, outputNode);

    }

    @Test
    public void filterJsonNode() throws IOException {

        JsonNode originalNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        List fieldsToBeEncrypted = Arrays.asList("userName", "name", "mobileNumber");

        JsonNode outputNode = JacksonUtils.filterJsonNodeWithFields(originalNode, fieldsToBeEncrypted);

        JsonNode expectedNode = mapper.readTree("{\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\"," +
                "\"mobileNumber\":\"12312312\"}}");

        assertEquals(expectedNode, outputNode);
    }

    @Test
    public void filterJsonNodeWithNoMatchingFields() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        List fieldsToBeEncrypted = Arrays.asList();

        JsonNode outputNode = JacksonUtils.filterJsonNodeWithFields(originalNode, fieldsToBeEncrypted);

        assertEquals(null, outputNode);

    }

    @Test
    public void checkIfAnyFieldExistsInJsonNode() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"tenantDetails\":{\"tenantId\":\"pb.amritsar\"}," +
                "\"name\":{\"firstName\":\"Customer Name\"}}");
        List<String> fields = Arrays.asList("firstName");
        assertEquals(false, JacksonUtils.checkIfNoFieldExistsInJsonNode(originalNode, fields));
    }

    @Test
    public void checkIfNoFieldExistsInJsonNode() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"tenantDetails\":{\"tenantId\":\"pb.amritsar\"}," +
                "\"name\":{\"firstName\":\"Customer Name\"}}");
        List<String> fields = Arrays.asList("asd");
        assertEquals(true, JacksonUtils.checkIfNoFieldExistsInJsonNode(originalNode, fields));
    }


    @Test
    public void filterJsonNodeForPathTest() throws IOException {
        JsonNode jsonNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":[{\"userName\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}," +
                "{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\",\"mobileNumber\":\"12312312\"," +
                "\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}]}");


//        JsonNode newNode = JacksonUtils.filterJsonNodeWithPaths(jsonNode, Arrays.asList("User/*/name", "RequestInfo" +
//                "/api_id", "*/User"));

        JsonNode newNode = JacksonUtils.filterJsonNodeWithPaths(jsonNode, Arrays.asList("*/ads"));

        log.info(String.valueOf(newNode));

        JsonNode expectedNode = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\"},\"User\":[{\"name\":null}," +
                "{\"name\":\"ajay\"}]}");

//        assertEquals(expectedNode, newNode);
    }




}