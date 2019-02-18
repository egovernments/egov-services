package org.egov.user.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.tracer.model.CustomException;
import org.egov.user.encryption.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class EncryptionService {

    @Autowired
    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${egov.enc.host}")
    private String egovEncHost;

    @Value("egov.enc.path.encryption")
    private String egovEncEncryptPath;

    @Value("egov.enc.path.decryption")
    private String egovEncDecryptPath;

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
        Iterator<String> iterator = fieldsAndTheirType.keySet().iterator();
        while (iterator.hasNext()) {
            String field = iterator.next();
            String type = fieldsAndTheirType.get(field);
            if(! typesAndFieldsToEncrypt.containsKey(type)) {
                List<String> fieldsToEncrypt = new ArrayList<>();
                typesAndFieldsToEncrypt.put(type, fieldsToEncrypt);
            }
            typesAndFieldsToEncrypt.get(type).add(field);
        }
    }

    public String encryptValue(String plaintext, String tenantId, String type) {
        return encryptValue(new ArrayList<String>(Collections.singleton(plaintext)), tenantId, type).get(0);
    }

    public List<String> encryptValue(List<String> plaintext, String tenantId, String type) {
        Object encryptionResponse = encryptionServiceRestInterface.callEncrypt(tenantId, type, plaintext);
        return (List<String>) encryptionResponse;
    }

    public ObjectNode encryptJson(Object plaintextJson, String tenantId) {

        JsonNode plaintextNode = createObjectNode(plaintextJson);
        JsonNode encryptedNode = plaintextNode.deepCopy();

        Iterator<String> iterator = typesAndFieldsToEncrypt.keySet().iterator();
        while (iterator.hasNext()) {
            String type = iterator.next();
            List<String> fields = typesAndFieldsToEncrypt.get(type);

            JsonNode jsonNode = JacksonUtils.filterJsonNode(plaintextNode, fields);
            JsonNode returnedEncryptedNode = (JsonNode) encryptionServiceRestInterface.callEncrypt(tenantId, type, jsonNode);

            encryptedNode = JacksonUtils.merge(returnedEncryptedNode, encryptedNode);
        }

        return (ObjectNode) encryptedNode;
    }

    public Object decryptValue(Object ciphertext) {
        return encryptionServiceRestInterface.callDecrypt(ciphertext);
    }

    public ObjectNode decryptJson(Object ciphertextJson, List<String> fields) {
        JsonNode ciphertextNode = createObjectNode(ciphertextJson);
        JsonNode decryptedNode = ciphertextNode.deepCopy();

        JsonNode jsonNode = JacksonUtils.filterJsonNode(ciphertextNode, fields);
        JsonNode returnedDecryptedNode = (JsonNode) encryptionServiceRestInterface.callDecrypt(ciphertextNode);

        decryptedNode = JacksonUtils.merge(returnedDecryptedNode, decryptedNode);

        return (ObjectNode) decryptedNode;
    }

    private ObjectNode createObjectNode(Object plaintextJson) {
        ObjectNode jsonNode = null;
        try {
            if(plaintextJson instanceof JsonNode)
                jsonNode = (ObjectNode) plaintextJson;
            else if(plaintextJson instanceof String)
                jsonNode = (ObjectNode) mapper.readTree((String) plaintextJson);           //JsonNode from JSON String
            else
                jsonNode = mapper.valueToTree(plaintextJson);                               //JsonNode from POJO
        } catch (Exception e) {
            throw new CustomException("Cannot convert to JsonNode : " + plaintextJson, "Cannot convert to JsonNode");
        }
        return jsonNode;
    }

}
