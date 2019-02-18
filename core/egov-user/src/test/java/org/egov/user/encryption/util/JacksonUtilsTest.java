package org.egov.user.encryption.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.egov.user.encryption.util.JacksonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class JacksonUtilsTest {

    ObjectMapper mapper;
    Configuration configuration;

    @Before
    public void initializeCommonObjects() {
        JsonFactory jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
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

        List fieldsToBeEncrypted = Arrays.asList(new String[] {"userName", "name", "mobileNumber"});

        JsonNode outputNode = JacksonUtils.filterJsonNode(originalNode, fieldsToBeEncrypted);

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

        List fieldsToBeEncrypted = Arrays.asList(new String[] {});

        JsonNode outputNode = JacksonUtils.filterJsonNode(originalNode, fieldsToBeEncrypted);

        assertEquals(null, outputNode);

    }



    @Test
    public void checkIfAnyFieldExistsInJsonNode() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"tenantDetails\":{\"tenantId\":\"pb.amritsar\"}," +
                "\"name\":{\"firstName\":\"Customer Name\"}}");
        List<String> fields = Arrays.asList(new String[] {"firstName"});
        assertEquals(false, JacksonUtils.checkIfNoFieldExistsInJsonNode(originalNode, fields));
    }

    @Test
    public void checkIfNoFieldExistsInJsonNode() throws IOException {
        JsonNode originalNode = mapper.readTree("{\"tenantDetails\":{\"tenantId\":\"pb.amritsar\"}," +
                "\"name\":{\"firstName\":\"Customer Name\"}}");
        List<String> fields = Arrays.asList(new String[] {"asd"});
        assertEquals(true, JacksonUtils.checkIfNoFieldExistsInJsonNode(originalNode, fields));
    }




}
