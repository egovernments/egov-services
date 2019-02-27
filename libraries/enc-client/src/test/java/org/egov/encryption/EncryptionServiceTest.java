package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@Slf4j
public class EncryptionServiceTest {

    ObjectMapper mapper;
    Configuration configuration;
    EncryptionService encryptionService;

    @Before
    public void initializeCommonObjects() {
        mapper = new ObjectMapper(new JsonFactory());
        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);

        Map<String, String> fieldsAndTheirType = new HashMap<>();
        fieldsAndTheirType.put("$.User.mobileNumber", "Normal");
        fieldsAndTheirType.put("$.User.name", "Normal");
        fieldsAndTheirType.put("$.User.userName", "Normal");

        encryptionService = new EncryptionService(new AbacFilter(Collections.EMPTY_LIST), fieldsAndTheirType);

    }

    @Test
    public void encryptStringValue() {
        String value = "Private";
        String tenantId = "pb";
        String type = "Normal";

        String ciphertext = encryptionService.encryptValue(value, tenantId, type);
        log.info(ciphertext);
    }

    @Test
    public void encryptJson() throws IOException {
        JsonNode plaintext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

//        JsonNode ciphertext = encryptionService.encryptJson(plaintext, "pb");
//        log.info(ciphertext.toString());
    }

    @Test
    public void decryptJson() throws IOException {
        JsonNode ciphertext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"638923|lN73WPVZfT7XhLAwgXSvc3Bnpa0=\"," +
                "\"name\":\"638923|lN73WPVZfT7XhLAwgXSvc3Bnpa0=\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"638923|xIalEH4yZMCIqvYlKH+0NLehuptUE8Wq\",\"active\":true,\"type\":\"CITIZEN\"," +
                "\"password\":\"password\"}}");

//        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Arrays.asList("$.User.mobileNumber", "$.User" +
//                ".name"), User.builder().build());
//        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Collections.EMPTY_LIST);
//        log.info(plaintext.toString());
    }

    @Test
    public void test() throws IOException {

    }



}