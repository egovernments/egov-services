package org.egov.encryption.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.EncryptionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EncryptionPolicyConfiguration {

    @Value("${egov.mdms.host}")
    private String egovMdmsHost;
    @Value("${egov.mdms.search.endpoint}")
    private String egovMdmsSearchEndpoint;

    private Map<String, List<Attribute>> keyAttributeMap;

    public EncryptionPolicyConfiguration() {
        initializeKeyAttributeMapFromMdms();
    }

    public EncryptionPolicyConfiguration(List<EncryptionPolicy> encryptionPolicyList) {
        initializeKeyAttributeMap(encryptionPolicyList);
    }

    private void initializeKeyAttributeMapFromMdms() {
        List<EncryptionPolicy> encryptionPolicyList = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
            String mdmsRequest = "{\"RequestInfo\":{},\"MdmsCriteria\":{\"tenantId\":\"pb\"," +
                    "\"moduleDetails\":[{\"moduleName\":\"DataSecurity\"," +
                    "\"masterDetails\":[{\"name\":\"EncryptionPolicy\"}]}]}}";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(egovMdmsHost + egovMdmsSearchEndpoint,
                    objectMapper.readTree(mdmsRequest), JsonNode.class);

            String policyListString = String.valueOf(response.getBody().get("MdmsRes").get(
                    "DataSecurity").get("EncryptionPolicy"));
            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                    EncryptionPolicy.class));
            encryptionPolicyList = reader.readValue(policyListString);
        } catch (IOException e) {}

        initializeKeyAttributeMap(encryptionPolicyList);
    }

    private void initializeKeyAttributeMap(List<EncryptionPolicy> encryptionPolicyList) {
        keyAttributeMap = encryptionPolicyList.stream().collect(Collectors
                        .toMap(EncryptionPolicy::getKey, EncryptionPolicy::getAttributeList));
    }

    public List<Attribute> getAttributesForKey(String key) {
        return keyAttributeMap.get(key);
    }

    public Map<String, List<Attribute>> getTypeAttributeMap(List<Attribute> attributeList) {
        Map<String, List<Attribute>> typeAttributeMap = new HashMap<>();
        for (Attribute attribute : attributeList) {
            String type = attribute.getType();
            if (! typeAttributeMap.containsKey(type)) {
                List<Attribute> attributesToEncrypt = new ArrayList<>();
                typeAttributeMap.put(type, attributesToEncrypt);
            }
            typeAttributeMap.get(type).add(attribute);
        }
        return typeAttributeMap;
    }
}
