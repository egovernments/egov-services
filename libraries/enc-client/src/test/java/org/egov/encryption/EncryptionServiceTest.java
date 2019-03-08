package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EncryptionServiceTest {

    ObjectMapper mapper;
    Configuration configuration;
    EncryptionService encryptionService;
    User user;

    @Before
    public void init() throws InstantiationException, IllegalAccessException, IOException {
        Role role1 = Role.builder().code("GRO").build();
        Role role2 = Role.builder().code("LME").build();
        user = User.builder().roles(Arrays.asList(role1, role2)).build();

        mapper = new ObjectMapper(new JsonFactory());

//        Map<String, String> fieldsAndTheirType = new HashMap<>();
//        fieldsAndTheirType.put("User/mobileNumber", "Normal");
//        fieldsAndTheirType.put("User/name", "Normal");
//        fieldsAndTheirType.put("User/userName", "Normal");
//
//        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
//
//        URL url = getClass().getClassLoader().getResource("DecryptionABAC.json");
//        String keyRoleAttributeAccessListString = new String(Files.readAllBytes(Paths.get(url.getPath())));
//        ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
//                KeyRoleAttributeAccess.class));
//        List<KeyRoleAttributeAccess> structureRoleAttributeAccessList = reader.readValue(keyRoleAttributeAccessListString);

        encryptionService = new EncryptionService();
    }

    @Ignore
    @Test
    public void encryptJsonObject() throws IOException {
        JsonNode plaintext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        JsonNode ciphertext = encryptionService.encryptJson(plaintext, "pb");
        log.info(ciphertext.toString());
    }

    @Ignore
    @Test
    public void encryptValueTest() throws IOException {
        log.info( encryptionService.encryptValue(1, "pb", "Normal") );
    }

    @Ignore
    @Test
    public void encryptJsonUsingKey() throws IOException {
        JsonNode plaintext = mapper.readTree("{\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\",\"ts\":null," +
                "\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null},\"User\":{\"userName\":\"ajay\",\"name\":\"ajay\",\"gender\":\"male\"," +
                "\"mobileNumber\":\"12312312\",\"active\":true,\"type\":\"CITIZEN\",\"password\":\"password\"}}");

        JsonNode ciphertext = encryptionService.encryptJson(plaintext, "User", "pb");
        log.info(ciphertext.toString());
    }

    @Ignore
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

    @Ignore
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

    @Ignore
    @Test
    public void decryptJsonArrayUsingRoles() throws IOException {
        JsonNode ciphertext = mapper.readTree("[{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}]");
        User user = User.builder().roles(Arrays.asList(Role.builder().code("GRO").build())).build();
        JsonNode plaintext = encryptionService.decryptJson(ciphertext, user, "PGR-Complaints-Report");
        log.info(plaintext.toString());
    }

    @Ignore
    @Test
    public void decryptJsonObjectUsingRoles() throws IOException {
        JsonNode ciphertext = mapper.readTree("{\"User\":{\"mobileNumber\":\"341642|WfYfJPRug15R2wFh17PlQr5d9YhNkFk1" +
                "\",\"name\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\"," +
                "\"userName\":\"341642|Ca5NbGHu3aB2ufjrNfZarW1VGBA=\",\"gender\":\"male\",\"active\":true," +
                "\"type\":\"CITIZEN\",\"password\":\"password\"},\"RequestInfo\":{\"api_id\":\"1\",\"ver\":\"1\"," +
                "\"ts\":null,\"action\":\"create\",\"did\":\"\",\"key\":\"\",\"msg_id\":\"\",\"requester_id\":\"\"," +
                "\"auth_token\":null}}");
        User user = User.builder().roles(Arrays.asList(Role.builder().code("GRO").build())).build();
        JsonNode plaintext = encryptionService.decryptJson(ciphertext, user, "PGR-Complaints-Report");
        log.info(plaintext.toString());
    }

    @Ignore
    @Test
    public void test() throws IOException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Rushang");
        user.put("mobileNumber", "187238917239");
        map.put("User", user);
        map = encryptionService.encryptJson(map, "pb", Map.class);
        log.info(String.valueOf(map));
    }


}