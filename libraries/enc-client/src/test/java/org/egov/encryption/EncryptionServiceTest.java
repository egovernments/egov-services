package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        fieldsAndTheirType.put("User/mobileNumber", "Normal");
        fieldsAndTheirType.put("User/name", "Normal");
        fieldsAndTheirType.put("User/userName", "Normal");

        encryptionService = new EncryptionService(fieldsAndTheirType);

    }

    @Test
    public void encryptStringValue() {
        String value = "Private";
        String tenantId = "pb";
        String type = "Normal";

//        String ciphertext = encryptionService.encryptValue(value, tenantId, type);
//        log.info(ciphertext);
    }

    @Test
    public void encryptJsonObject() throws IOException {
        JsonNode plaintext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        JsonNode ciphertext = encryptionService.encryptJson(plaintext, "pb");
        log.info(ciphertext.toString());
    }

    @Test
    public void decryptJsonObject() throws IOException {
        JsonNode ciphertext = mapper.readTree("{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}");

        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Arrays.asList("User/mobileNumber", "User" +
                "/name"), User.builder().build());
        log.info(plaintext.toString());
    }

    @Test
    public void decryptJsonArray() throws IOException {
        JsonNode ciphertext = mapper.readTree("[{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}]");

        JsonNode plaintext = encryptionService.decryptJson(ciphertext, Arrays.asList("*/User/mobileNumber", "*/User" +
                "/name"), User.builder().build());
        log.info(plaintext.toString());
    }

    @Test
    public void test() throws IOException {

    }



}