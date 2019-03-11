package org.egov.encryption.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.egov.encryption.models.RoleAttributeAccess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbacConfiguration {

    @Value("${egov.mdms.host}")
    private String egovMdmsHost;
    @Value("${egov.mdms.search.endpoint}")
    private String egovMdmsSearchEndpoint;

    private Map<String, List<RoleAttributeAccess>> keyRoleAttributeAccessMap;

    public AbacConfiguration() {
        initializeKeyRoleAttributeAccessMapFromMdms();
    }


    public AbacConfiguration(List<KeyRoleAttributeAccess> keyRoleAttributeAccessList) {
        initializeKeyRoleAttributeAccessMap(keyRoleAttributeAccessList);
    }

    private void initializeKeyRoleAttributeAccessMap(List<KeyRoleAttributeAccess> keyRoleAttributeAccessList) {
        keyRoleAttributeAccessMap = keyRoleAttributeAccessList.stream()
                .collect(Collectors.toMap(KeyRoleAttributeAccess::getKey,
                        KeyRoleAttributeAccess::getRoleAttributeAccessList));
    }

    private void initializeKeyRoleAttributeAccessMapFromMdms() {
        List<KeyRoleAttributeAccess> keyRoleAttributeAccessList = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

            String mdmsRequest = "{\"RequestInfo\":{},\"MdmsCriteria\":{\"tenantId\":\"pb\"," +
                    "\"moduleDetails\":[{\"moduleName\":\"DataSecurity\"," +
                    "\"masterDetails\":[{\"name\":\"DecryptionABAC\"}]}]}}";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(egovMdmsHost + egovMdmsSearchEndpoint,
                    objectMapper.readTree(mdmsRequest), JsonNode.class);

            String keyRoleAttributeAccessListString = String.valueOf(response.getBody().get("MdmsRes").get(
                    "DataSecurity").get("DecryptionABAC"));

            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                    KeyRoleAttributeAccess.class));
            keyRoleAttributeAccessList = reader.readValue(keyRoleAttributeAccessListString);
        } catch (IOException e) {}

        initializeKeyRoleAttributeAccessMap(keyRoleAttributeAccessList);
    }


    public List<RoleAttributeAccess> getRoleAttributeAccessListForKey(String keyId) {
        return keyRoleAttributeAccessMap.get(keyId);
    }
}
