package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.egov.encryption.config.AbacConfiguration;
import org.egov.encryption.config.EncryptionPolicyConfiguration;
import org.egov.encryption.masking.MaskingService;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.egov.encryption.util.ConvertClass;
import org.egov.encryption.util.JSONBrowseUtil;
import org.egov.encryption.util.JacksonUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EncryptionService {

    private EncryptionServiceRestConnection encryptionServiceRestConnection;

    private EncryptionPolicyConfiguration encryptionPolicyConfiguration;
    private AbacConfiguration abacConfiguration;
    private AbacFilter abacFilter;
    private MaskingService maskingService;

    private Map<String, String> fieldsAndTheirType;
    private Map<String, List<String>> typesAndFieldsToEncrypt;

    private ObjectMapper objectMapper;

    public EncryptionService() throws IllegalAccessException, InstantiationException {
        this.encryptionPolicyConfiguration = new EncryptionPolicyConfiguration();
        this.abacConfiguration = new AbacConfiguration();
        this.abacFilter = new AbacFilter();
        maskingService = new MaskingService();
        encryptionServiceRestConnection = new EncryptionServiceRestConnection();
        objectMapper = new ObjectMapper(new JsonFactory());

    }

    public EncryptionService(Map<String, String> fieldsAndTheirType) throws IllegalAccessException,
            InstantiationException {
        this();
        this.fieldsAndTheirType = fieldsAndTheirType;
        initializeTypesAndFieldsToEncrypt();
    }

    EncryptionService(Map<String, String> fieldsAndTheirType, List<KeyRoleAttributeAccess> keyRoleAttributeAccessList)
            throws IllegalAccessException, InstantiationException {
        this(fieldsAndTheirType);
        this.abacConfiguration = new AbacConfiguration(keyRoleAttributeAccessList);
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

    public JsonNode encryptJson(Object plaintextJson, String key, String tenantId) throws IOException {

        JsonNode plaintextNode = createJsonNode(plaintextJson);
        JsonNode encryptNode = plaintextNode.deepCopy();

        List<Attribute> attributesToEncrypt = encryptionPolicyConfiguration.getAttributesForKey(key);
        Map<String, List<Attribute>> typeAttributeMap = encryptionPolicyConfiguration.getTypeAttributeMap(attributesToEncrypt);

        for (String type : typeAttributeMap.keySet()) {
            List<Attribute> attributes = typeAttributeMap.get(type);
            List<String> paths = attributes.stream().map(Attribute::getJsonPath).collect(Collectors.toList());

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(plaintextNode, paths);

            if(! jsonNode.isEmpty(objectMapper.getSerializerProvider())) {
                JsonNode returnedEncryptedNode = objectMapper.valueToTree(encryptionServiceRestConnection.callEncrypt(tenantId,
                        type, jsonNode));
                encryptNode = JacksonUtils.merge(returnedEncryptedNode, encryptNode);
            }
        }

        return encryptNode;
    }

    public <T> T encryptJson(Object plaintextJson, String key, String tenantId, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(encryptJson(plaintextJson, key, tenantId), valueType);
    }

    @Deprecated
    public JsonNode encryptJson(Object plaintextJson, String tenantId) throws IOException {

        JsonNode plaintextNode = createJsonNode(plaintextJson);
        JsonNode encryptNode = plaintextNode.deepCopy();

        for (String type : typesAndFieldsToEncrypt.keySet()) {
            List<String> paths = typesAndFieldsToEncrypt.get(type);

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(plaintextNode, paths);

            if(! jsonNode.isEmpty(objectMapper.getSerializerProvider())) {
                JsonNode returnedEncryptedNode = objectMapper.valueToTree(encryptionServiceRestConnection.callEncrypt(tenantId,
                        type, jsonNode));
                encryptNode = JacksonUtils.merge(returnedEncryptedNode, encryptNode);
            }
        }

        return encryptNode;
    }

    @Deprecated
    public <T> T encryptJson(Object plaintextJson, String tenantId, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(encryptJson(plaintextJson, tenantId), valueType);
    }


    public JsonNode decryptJson(Object ciphertextJson, Map<Attribute, AccessType> attributeAccessTypeMap, User user)
            throws IOException {
        JsonNode ciphertextNode = createJsonNode(ciphertextJson);
        JsonNode decryptNode = ciphertextNode.deepCopy();

        if(attributeAccessTypeMap.containsValue(AccessType.NONE)) {
            List<Attribute> attributesToBeRemoved = getKeysForValue(attributeAccessTypeMap, AccessType.NONE);
            List<String> pathsToBeRemoved = attributesToBeRemoved.stream().map(Attribute::getJsonPath).collect(Collectors.toList());
            JsonNode nodeToBeEmptied = JacksonUtils.filterJsonNodeWithPaths(decryptNode, pathsToBeRemoved);
            JsonNode emptyNode = JSONBrowseUtil.mapValues(nodeToBeEmptied, __ -> "");          //Empty String value
            decryptNode = JacksonUtils.merge(emptyNode, decryptNode);
        }

        List<Attribute> attributesToBeDecrypted = new ArrayList<>();
        for(Attribute attribute : attributeAccessTypeMap.keySet()) {
            AccessType accessType = attributeAccessTypeMap.get(attribute);
            if(accessType != AccessType.NONE)
                attributesToBeDecrypted.add(attribute);
        }
        List<String> pathsToBeDecrypted = attributesToBeDecrypted.stream().map(Attribute::getJsonPath).collect(Collectors.toList());

        JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(ciphertextNode, pathsToBeDecrypted);
        if(! jsonNode.isEmpty(objectMapper.getSerializerProvider())) {
            JsonNode returnedDecryptedNode = objectMapper.valueToTree(encryptionServiceRestConnection.callDecrypt(jsonNode));
            decryptNode = JacksonUtils.merge(returnedDecryptedNode, decryptNode);
        }

        if(attributeAccessTypeMap.containsValue(AccessType.MASK)) {
            List<Attribute> attributesToBeMasked = getKeysForValue(attributeAccessTypeMap, AccessType.MASK);
            decryptNode = maskingService.maskData(decryptNode, attributesToBeMasked);
        }

        return decryptNode;
    }

    @Deprecated
    public JsonNode decryptJson(Object ciphertextJson, List<String> paths, User user) throws IOException {

        Map<Attribute, AccessType> attributeAccessTypeMap =
                paths.stream().collect(Collectors.toMap(Attribute::new, __ -> AccessType.PLAIN));

        return decryptJson(ciphertextJson, attributeAccessTypeMap, user);
    }

    @Deprecated
    public <T> T decryptJson(Object ciphertextJson, List<String> paths, User user, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, paths, user), valueType);
    }


    public JsonNode decryptJson(Object ciphertextJson, String key, User user) throws IOException {

        List<String> roles = user.getRoles().stream().map(Role::getCode).collect(Collectors.toList());

        Map<Attribute, AccessType> attributeAccessTypeMap = abacFilter.getAttributeAccessForRoles(roles,
                abacConfiguration.getRoleAttributeAccessListForKey(key));

        JsonNode decryptedNode = decryptJson(ciphertextJson, attributeAccessTypeMap, user);

        return decryptedNode;
    }

    public <T> T decryptJson(Object ciphertextJson, String key, User user, Class<T> valueType) throws IOException {
        return ConvertClass.convertTo(decryptJson(ciphertextJson, key, user), valueType);
    }

    <K, V> List<K> getKeysForValue(Map<K, V> map, V findValue) {
        List<K> foundKeys = new ArrayList<>();
        map.forEach( (key, value) -> {
            if(value.equals(findValue))
                foundKeys.add(key);
        });
        return foundKeys;
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


    public String encryptValue(Object plaintext, String tenantId, String type) throws IOException {
        return encryptValue(new ArrayList<>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    public List<String> encryptValue(List<Object> plaintext, String tenantId, String type) throws IOException {
        Object encryptionResponse = encryptionServiceRestConnection.callEncrypt(tenantId, type, plaintext);
        return ConvertClass.convertTo(objectMapper.valueToTree(encryptionResponse), List.class);
    }


}