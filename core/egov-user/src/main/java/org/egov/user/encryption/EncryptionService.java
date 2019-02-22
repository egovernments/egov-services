package org.egov.user.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.egov.user.encryption.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EncryptionService {

    @Autowired
    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    @Value("#{${egov.enc.field.type.map}}")
    private Map<String, String> fieldsAndTheirType;

    private Map<String, List<String>> typesAndFieldsToEncrypt;

    private ObjectMapper mapper;


    public EncryptionService() {
        mapper = new ObjectMapper(new JsonFactory());
    }

    @PostConstruct
    public void initializeTypesAndFieldsToEncrypt() {
        typesAndFieldsToEncrypt = new HashMap<>();
        for (String field : fieldsAndTheirType.keySet()) {
            String type = fieldsAndTheirType.get(field);
            if (!typesAndFieldsToEncrypt.containsKey(type)) {
                List<String> fieldsToEncrypt = new ArrayList<>();
                typesAndFieldsToEncrypt.put(type, fieldsToEncrypt);
            }
            typesAndFieldsToEncrypt.get(type).add(field);
        }
    }

    public ObjectNode encryptJson(Object plaintextJson, String tenantId) throws IOException {

        JsonNode plaintextNode = createObjectNode(plaintextJson);
        JsonNode encryptedNode = plaintextNode.deepCopy();

        Iterator<String> iterator = typesAndFieldsToEncrypt.keySet().iterator();
        while (iterator.hasNext()) {
            String type = iterator.next();
            List<String> fields = typesAndFieldsToEncrypt.get(type);

            JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(plaintextNode, fields);
            if(jsonNode == null)
                continue;
            JsonNode returnedEncryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callEncrypt(tenantId, type,
                    jsonNode));

            encryptedNode = JacksonUtils.mergeNodesForGivenPaths(returnedEncryptedNode, encryptedNode, fields);
        }

        return (ObjectNode) encryptedNode;
    }

    public ObjectNode decryptJson(Object ciphertextJson, List<String> fields) throws IOException {
        JsonNode ciphertextNode = createObjectNode(ciphertextJson);
        JsonNode decryptedNode = ciphertextNode.deepCopy();

        JsonNode jsonNode = JacksonUtils.filterJsonNodeWithPaths(ciphertextNode, fields);
        if(jsonNode != null) {
            JsonNode returnedDecryptedNode = mapper.valueToTree(encryptionServiceRestInterface.callDecrypt(jsonNode));
            decryptedNode = JacksonUtils.mergeNodesForGivenPaths(returnedDecryptedNode, decryptedNode, fields);
        }

        return (ObjectNode) decryptedNode;
    }

    private ObjectNode createObjectNode(Object plaintextJson) {
        ObjectNode jsonNode = null;
        try {
            if(plaintextJson instanceof ObjectNode)
                jsonNode = (ObjectNode) plaintextJson;
            else if(plaintextJson instanceof String)
                jsonNode = (ObjectNode) mapper.readTree((String) plaintextJson);           //JsonNode from JSON String
            else
                jsonNode = mapper.valueToTree(plaintextJson);                               //JsonNode from POJO or Map
        } catch (Exception e) {
            throw new CustomException("Cannot convert to JsonNode : " + plaintextJson, "Cannot convert to JsonNode");
        }
        return jsonNode;
    }


    public String encryptValue(String plaintext, String tenantId, String type) {
        return encryptValue(new ArrayList<String>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    public List<String> encryptValue(List<String> plaintext, String tenantId, String type) {
        Object encryptionResponse = encryptionServiceRestInterface.callEncrypt(tenantId, type, plaintext);
        return (List<String>) encryptionResponse;
    }

    public Object decryptValue(Object ciphertext) {
        return encryptionServiceRestInterface.callDecrypt(ciphertext);
    }


}
