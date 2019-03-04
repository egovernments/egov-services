package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.encryption.accesscontrol.AbacFilter;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.util.JSONUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EncryptionService {

    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    private AbacFilter abacFilter;

    private Map<String, String> fieldsAndTheirType;

    private Map<String, List<String>> typesAndFieldsToEncrypt;

    private ObjectMapper mapper;


    public EncryptionService(AbacFilter abacFilter, Map<String, String> fieldsAndTheirType) {
        this.abacFilter = abacFilter;
        this.fieldsAndTheirType = fieldsAndTheirType;
        encryptionServiceRestInterface = new EncryptionServiceRestInterface();
        mapper = new ObjectMapper(new JsonFactory());
        initializeTypesAndFieldsToEncrypt();
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
            List<String> fields = typesAndFieldsToEncrypt.get(type);

            ArrayNode arrayNode = JSONUtils.filterJsonNodeWithPaths(plaintextNode, fields);
            if (arrayNode.isEmpty(mapper.getSerializerProvider()))
                continue;
            JsonNode returnedEncryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callEncrypt(tenantId,
                    type, arrayNode));

            encryptedNode = JSONUtils.mergeNodesForGivenPaths(returnedEncryptedNode, encryptedNode, fields);
        }

        return encryptedNode;
    }

    public JsonNode decryptJson(Object ciphertextJson, List<String> paths, User user) throws IOException {
        JsonNode ciphertextNode = createJsonNode(ciphertextJson);
        JsonNode decryptedNode = ciphertextNode.deepCopy();

        ArrayNode arrayNode = JSONUtils.filterJsonNodeWithPaths(ciphertextNode, paths);
        if(! arrayNode.isEmpty(mapper.getSerializerProvider())) {
            JsonNode returnedDecryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callDecrypt(arrayNode));
            decryptedNode = JSONUtils.mergeNodesForGivenPaths(returnedDecryptedNode, decryptedNode, paths);
        }

        return decryptedNode;
    }


    public JsonNode decryptJson(Object ciphertextJson, User user) throws IOException {

        Map<Attribute, AccessType> attributeAccessTypeMap = abacFilter.getAttributeAccessForRole(user.getRoles());
        List<String> paths = attributeAccessTypeMap.keySet().stream()
                .map(Attribute::getJsonPath).collect(Collectors.toList());

        JsonNode decryptedNode = decryptJson(ciphertextJson, paths, user);

        return decryptedNode;
    }

    JsonNode createJsonNode(Object json) {
        JsonNode jsonNode = null;
        try {
            if(json instanceof JsonNode)
                jsonNode = (JsonNode) json;
            else if(json instanceof String)
                jsonNode = mapper.readTree((String) json);           //JsonNode from JSON String
            else
                jsonNode = mapper.valueToTree(json);                 //JsonNode from POJO or Map
        } catch (Exception e) {
            log.error(e.getMessage());
//            throw new CustomException("Cannot convert to JsonNode : " + json, "Cannot convert to JsonNode");
        }
        return jsonNode;
    }


    String encryptValue(String plaintext, String tenantId, String type) {
        return encryptValue(new ArrayList<String>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    List<String> encryptValue(List<String> plaintext, String tenantId, String type) {
        Object encryptionResponse = encryptionServiceRestInterface.callEncrypt(tenantId, type, plaintext);
        return (List<String>) encryptionResponse;
    }

    Object decryptValue(Object ciphertext) {
        return encryptionServiceRestInterface.callDecrypt(ciphertext);
    }


}