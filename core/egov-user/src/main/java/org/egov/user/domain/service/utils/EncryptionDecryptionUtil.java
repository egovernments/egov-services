package org.egov.user.domain.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.tracer.model.CustomException;
import org.egov.user.domain.model.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class EncryptionDecryptionUtil
{
    private EncryptionService encryptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("#{${egov.enc.field.type.map}}")
    private HashMap<String,String> dec_fields_mapForObjects;

    @Value("#{${egov.dec.field.type.map}}")
    private HashMap<String,String> dec_fields_map_ForList;

    @Value(("${egov.state.level.tenant.id}"))
    private String stateLevelTenantId;

    public EncryptionDecryptionUtil(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public <T>T encryptObject(Object objectToEncrypt, Class<T> classType)
    {
        try {
            JsonNode encryptedObject = encryptionService.encryptJson(objectToEncrypt, stateLevelTenantId);
            return objectMapper.treeToValue(encryptedObject, classType);
        }
        catch (JsonProcessingException e)
        {
            log.error("JsonProcessing error occurred while enrypting",e);
            throw new CustomException("ENCRYPTION_ERROR","JsonProcessing error occurred while enrypting");
        } catch (IOException e) {
            throw new CustomException("ENCRYPTION_ERROR","IO error occurred while encrypting");
        }
    }

    public <E,P>P decryptObject(Object objectToDecrypt, Class<E> classType, User userInfo)
    {
        try {
            if(objectToDecrypt instanceof List)
            {
                JsonNode  decryptedObject = encryptionService.decryptJson(objectToDecrypt,new ArrayList<>(dec_fields_map_ForList.keySet()),userInfo);
                ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,classType));
                return reader.readValue(decryptedObject);
            }
            else {

                JsonNode  decryptedObject = encryptionService.decryptJson(objectToDecrypt,new ArrayList<>(dec_fields_mapForObjects.keySet()),userInfo);
                return (P)objectMapper.treeToValue(decryptedObject, classType);
            }
        } catch (IOException e) {
            throw new CustomException("DECRYPTION_ERROR","IO error occurred while decrypting");
        }
    }
}
