package org.egov.user.encryption;

import org.egov.user.web.contract.EncReqObject;
import org.egov.user.web.contract.EncryptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EncryptionServiceRestInterface {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${egov.enc.host}")
    private String egovEncHost;

    @Value("${egov.enc.path.encryption}")
    private String egovEncEncryptPath;

    @Value("${egov.enc.path.decryption}")
    private String egovEncDecryptPath;


    Object callEncrypt(String tenantId, String type, Object value) {

        EncReqObject encReqObject = new EncReqObject(tenantId, type, value);
        EncryptionRequest encryptionRequest = new EncryptionRequest();
        encryptionRequest.setEncryptionRequests(new ArrayList<EncReqObject>(Collections.singleton(encReqObject)));

        ResponseEntity<List> response =  restTemplate.postForEntity(egovEncHost + egovEncEncryptPath, encryptionRequest,
                List.class);

        return response.getBody().get(0);
    }

    Object callDecrypt(Object ciphertext) {
        ResponseEntity<Object> response =  restTemplate.postForEntity(egovEncHost + egovEncDecryptPath,
                ciphertext, Object.class);

        return response.getBody();
    }


}
