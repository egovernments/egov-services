package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.egov.encryption.config.KeyRoleAttributeAccessConfiguration;
import org.egov.encryption.masking.MaskingService;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.egov.encryption.models.RoleAttributeAccess;
import org.egov.encryption.util.ConvertClass;
import org.egov.encryption.util.JacksonUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EncryptionService {

    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    private KeyRoleAttributeAccessConfiguration keyRoleAttributeAccessConfiguration;
    private AbacFilter abacFilter;
    private MaskingService maskingService;

    private Map<String, String> fieldsAndTheirType;
    private Map<String, List<String>> typesAndFieldsToEncrypt;

    private ObjectMapper objectMapper;

    public EncryptionService(Map<String, String> fieldsAndTheirType) throws IllegalAccessException,
            InstantiationException {
        this.keyRoleAttributeAccessConfiguration = new KeyRoleAttributeAccessConfiguration();
        this.abacFilter = new AbacFilter();
        maskingService = new MaskingService();
        encryptionServiceRestInterface = new EncryptionServiceRestInterface();
        objectMapper = new ObjectMapper(new JsonFactory());

        this.fieldsAndTheirType = fieldsAndTheirType;
        initializeTypesAndFieldsToEncrypt();
    }

    EncryptionService(Map<String, String> fieldsAndTheirType, List<KeyRoleAttributeAccess> keyRoleAttributeAccessList)
            throws IllegalAccessException,
            InstantiationException {
        this(fieldsAndTheirType);
        this.keyRoleAttributeAccessConfiguration = new KeyRoleAttributeAccessConfiguration(keyRoleAttributeAccessList);
    }

    void initializeTypesAndFieldsToEncrypt() {
        typesAndFieldsToEncrypt = new HashMap<>();
        for (String field : fieldsAndTheirType.keySet()) {
            String type = fieldsAndTheirType.get(field);
            if (!typesAndFieldsToEncrypt.containsKey(type)) {
                List<String> fieldsToEncrypt = new ArrayList<String>();
                typesAndFieldsToEncrypt.put(type, fieldsToEncrypt);
            }
            typesAndFieldsToEncrypt.get(type).add(field);
        }
    }

    public JsonNode encryptJson(Object plaintextJson, String tenantId) throws IOException {

        JsonNode plaintextNode = createJsonNode(plaintextJson);
        JsonNode encryptedNode = plaintextNode.deepCopy();

        for (String type : typesAndFieldsToEncrypt.keySet()) {
            List<String> paths = typesAndFieldsToEncrypt.get(type);

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(plaintextNode, paths);

            if(! jsonNode.isEmpty(objectMapper.getSerializerProvider())) {
                JsonNode returnedEncryptedNode = objectMapper.valueToTree(encryptionServiceRestInterface.callEncrypt(tenantId,
                        type, jsonNode));
                encryptedNode = JacksonUtils.merge(returnedEncryptedNode, encryptedNode);
            }
        }

        return encryptedNode;
    }

    public <T> T encryptJson(Object plaintextJson, String tenantId, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(encryptJson(plaintextJson, tenantId), valueType);
    }


    public JsonNode decryptJson(Object ciphertextJson, Map<Attribute, AccessType> attributeAccessTypeMap, User user)
            throws IOException {
        JsonNode ciphertextNode = createJsonNode(ciphertextJson);
        JsonNode decryptedNode = ciphertextNode.deepCopy();

        List<String> paths = attributeAccessTypeMap.keySet().stream()
                .map(Attribute::getJsonPath).collect(Collectors.toList());

        JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(ciphertextNode, paths);
        if(! jsonNode.isEmpty(objectMapper.getSerializerProvider())) {
            JsonNode returnedDecryptedNode = objectMapper.valueToTree(encryptionServiceRestInterface.callDecrypt(jsonNode));

            if(attributeAccessTypeMap.containsValue(AccessType.MASK)) {
                List<Attribute> attributesToBeMasked = getKeysForValue(attributeAccessTypeMap, AccessType.MASK);
                returnedDecryptedNode = maskingService.maskData(returnedDecryptedNode, attributesToBeMasked);
            }

            decryptedNode = JacksonUtils.merge(returnedDecryptedNode, decryptedNode);
        }


        return decryptedNode;
    }

    <K, V> List<K> getKeysForValue(Map<K, V> map, V findValue) {
        List<K> foundKeys = new ArrayList<>();
        map.forEach( (key, value) -> {
            if(value.equals(findValue))
                foundKeys.add(key);
        });
        return foundKeys;
    }

    public JsonNode decryptJson(Object ciphertextJson, List<String> paths, User user) throws IOException {

        Map<Attribute, AccessType> attributeAccessTypeMap =
                paths.stream().collect(Collectors.toMap(Attribute::new, __ -> AccessType.PLAIN));

        return decryptJson(ciphertextJson, attributeAccessTypeMap, user);
    }

    public <T> T decryptJson(Object ciphertextJson, List<String> paths, User user, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, paths, user), valueType);
    }


    public JsonNode decryptJson(Object ciphertextJson, User user, String keyId) throws IOException {

        List<String> roles = user.getRoles().stream().map(Role::getCode).collect(Collectors.toList());

        Map<Attribute, AccessType> attributeAccessTypeMap = abacFilter.getAttributeAccessForRoles(roles,
                keyRoleAttributeAccessConfiguration.getRoleAttributeAccessListForKey(keyId));

        JsonNode decryptedNode = decryptJson(ciphertextJson, attributeAccessTypeMap, user);

        return decryptedNode;
    }

    public <T> T decryptJson(Object ciphertextJson, User user, String keyId, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, user, keyId), valueType);
    }

    JsonNode createJsonNode(Object json) throws IOException {
        JsonNode jsonNode;
        if(json instanceof JsonNode)
            jsonNode = (JsonNode) json;
        else if(json instanceof String)
            jsonNode = objectMapper.readTree((String) json);           //JsonNode from JSON String
        else
            jsonNode = objectMapper.valueToTree(json);                 //JsonNode from POJO or Map
        return jsonNode;
    }


    String encryptValue(String plaintext, String tenantId, String type) throws IOException {
        return encryptValue(new ArrayList<String>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    List<String> encryptValue(List<String> plaintext, String tenantId, String type) throws IOException {
        Object encryptionResponse = encryptionServiceRestInterface.callEncrypt(tenantId, type, plaintext);
        return (List<String>) encryptionResponse;
    }

    Object decryptValue(Object ciphertext) throws IOException {
        return encryptionServiceRestInterface.callDecrypt(ciphertext);
    }


}