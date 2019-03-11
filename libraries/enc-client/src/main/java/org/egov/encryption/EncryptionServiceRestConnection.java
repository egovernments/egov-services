package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.encryption.web.contract.EncReqObject;
import org.egov.encryption.web.contract.EncryptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@AllArgsConstructor
public class EncryptionServiceRestConnection {

    @Value("${egov.enc.host}")
    private String egovEncHost;
    @Value("${egov.enc.encrypt.endpoint}")
    private String egovEncEncryptPath;
    @Value("${egov.enc.decrypt.endpoint}")
    private String egovEncDecryptPath;

    private RestTemplate restTemplate;

    private ObjectMapper mapper;

    public EncryptionServiceRestConnection() {
        mapper = new ObjectMapper(new JsonFactory());
        restTemplate = new RestTemplate();

        // TODO Remove hard-coded urls
        egovEncHost = "http://localhost:1234";
        egovEncEncryptPath = "/egov-enc-service/crypto/v1/_encrypt";
        egovEncDecryptPath = "/egov-enc-service/crypto/v1/_decrypt";
    }

    Object callEncrypt(String tenantId, String type, Object value) throws IOException {

        EncReqObject encReqObject = new EncReqObject(tenantId, type, value);
        EncryptionRequest encryptionRequest = new EncryptionRequest();
        encryptionRequest.setEncryptionRequests(new ArrayList<>(Collections.singleton(encReqObject)));

        ResponseEntity<String> response = restTemplate.postForEntity(egovEncHost + egovEncEncryptPath,
                encryptionRequest, String.class);
        return mapper.readTree(response.getBody()).get(0);
    }

    Object callDecrypt(Object ciphertext) throws IOException {
        ResponseEntity<String> response = restTemplate.postForEntity(egovEncHost + egovEncDecryptPath,
                ciphertext, String.class);
        return mapper.readTree(response.getBody());
    }

}