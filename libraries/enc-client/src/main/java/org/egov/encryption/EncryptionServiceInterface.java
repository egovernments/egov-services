package org.egov.encryption;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.common.contract.request.User;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EncryptionServiceInterface {

    public JsonNode encryptJson(Object plaintextJson, String key, String tenantId) throws IOException;

    public <T> T encryptJson(Object plaintextJson, String key, String tenantId, Class<T> valueType) throws IOException;

    public JsonNode decryptJson(Object ciphertextJson, Map<Attribute, AccessType> attributeAccessTypeMap, User user) throws IOException;

    public JsonNode decryptJson(Object ciphertextJson, String key, User user) throws IOException;

    public <T> T decryptJson(Object ciphertextJson, User user, String key, Class<T> valueType) throws IOException;

    public String encryptValue(Object plaintext, String tenantId, String type) throws IOException;

    public List<String> encryptValue(List<Object> plaintext, String tenantId, String type) throws IOException;

}
