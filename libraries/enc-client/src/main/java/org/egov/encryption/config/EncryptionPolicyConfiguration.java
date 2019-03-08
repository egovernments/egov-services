package org.egov.encryption.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.EncryptionPolicy;
import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            URL url = getClass().getClassLoader().getResource("EncryptionPolicy.json");
            String keyRoleAttributeAccessListString = new String(Files.readAllBytes(Paths.get(url.getPath())));
            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                    EncryptionPolicy.class));
            encryptionPolicyList = reader.readValue(keyRoleAttributeAccessListString);
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
