package org.egov.enc.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.enc.KeyManagementApplication;
import org.egov.enc.services.EncryptionService;
import org.egov.enc.web.models.EncryptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
public class CryptoApiController{

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private KeyManagementApplication keyManagementApplication;

    @Autowired
    public CryptoApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value="/crypto/v1/_encrypt", method = RequestMethod.POST)
    public ResponseEntity<Object> cryptoV1EncryptPost(@Valid @RequestBody EncryptionRequest encryptionRequest) throws Exception {
        return new ResponseEntity<>(encryptionService.encrypt(encryptionRequest), HttpStatus.OK );
    }

    @RequestMapping(value="/crypto/v1/_decrypt", method = RequestMethod.POST)
    public ResponseEntity<Object> cryptoV1DecryptPost(@Valid @RequestBody Object decryptReqObject) throws Exception {
        return new ResponseEntity<>(encryptionService.decrypt(decryptReqObject), HttpStatus.OK );
    }

}
