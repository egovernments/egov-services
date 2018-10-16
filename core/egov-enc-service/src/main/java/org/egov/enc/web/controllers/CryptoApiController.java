package org.egov.enc.web.controllers;


import org.egov.enc.KeyManagementApplication;
import org.egov.enc.services.EncryptionService;
import org.egov.enc.web.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;

import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-10-11T17:31:52.360+05:30")

@Controller
public class CryptoApiController{

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private EncryptionService encryptionService;

    private KeyManagementApplication keyManagementApplication;

    @Autowired
    public CryptoApiController(ObjectMapper objectMapper, HttpServletRequest request, EncryptionService encryptionService, KeyManagementApplication keyManagementApplication) {
        this.objectMapper = objectMapper;
        this.request = request;

        this.encryptionService = encryptionService;
        this.keyManagementApplication = keyManagementApplication;
    }

    @RequestMapping(value="/crypto/v1/_encrypt", method = RequestMethod.POST)
    public ResponseEntity<Object> cryptoV1EncryptPost(@Valid @RequestBody EncryptReqObject encryptReqObject) {
        try {
            if(!keyManagementApplication.checkTenant(encryptReqObject.getTenantId())) {
                throw new CustomException("Tenant Does Not Exist", "Tenant Does Not Exist");
            }
            return new ResponseEntity<>(encryptionService.encrypt(encryptReqObject), HttpStatus.OK );
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value="/crypto/v1/_decrypt", method = RequestMethod.POST)
    public ResponseEntity<Object> cryptoV1DecryptPost(@Valid @RequestBody Object decryptReqObject) {
        try {
            return new ResponseEntity<>(encryptionService.decrypt(decryptReqObject), HttpStatus.OK );
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/crypto/v1/_sign", method = RequestMethod.POST)
    public ResponseEntity<Signature> cryptoV1SignPost(@Valid @RequestBody SignRequest signRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Signature>(objectMapper.readValue("{  \"keyId\" : 0,  \"version\" : \"version\",  \"signatureValue\" : \"signatureValue\"}", Signature.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<Signature>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Signature>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/crypto/v1/_verify", method = RequestMethod.POST)
    public ResponseEntity<InlineResponse200> cryptoV1VerifyPost( @Valid @RequestBody VerifyRequest verifyRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<InlineResponse200>(objectMapper.readValue("{  \"verified\" : true}", InlineResponse200.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<InlineResponse200>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<InlineResponse200>(HttpStatus.NOT_IMPLEMENTED);
    }
}
