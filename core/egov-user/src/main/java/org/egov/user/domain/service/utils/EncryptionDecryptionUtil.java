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

    @Value(("${egov.state.level.tenant.id}"))
    private String stateLevelTenantId;

    public EncryptionDecryptionUtil(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public <T>T encryptObject(Object objectToEncrypt, String key,Class<T> classType)
    {
        try {
            return encryptionService.encryptJson(objectToEncrypt,key,stateLevelTenantId,classType);
        } catch (IOException e) {
            log.error("IO error occurred while decrypting",e);
            throw new CustomException("DECRYPTION_ERROR","IO error occurred while decrypting");
        }
    }

    public <E,P>P decryptObject(Object objectToDecrypt, String key, Class<E> classType, User userInfo)
    {

        try {
            return  (P)encryptionService.decryptJson(objectToDecrypt,key,userInfo,classType);
        } catch (IOException e) {
            log.error("IO error occurred while decrypting",e);
            throw new CustomException("DECRYPTION_ERROR","IO error occurred while decrypting");
        }
    }
}
