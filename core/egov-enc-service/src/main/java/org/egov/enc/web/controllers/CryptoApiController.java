package org.egov.enc.web.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import org.egov.enc.KeyManagementApplication;
import org.egov.enc.keymanagement.KeyStore;
import org.egov.enc.services.AESEncryptionService;
import org.egov.enc.services.EncryptionService;
import org.egov.enc.services.RSAEncryptionService;
import org.egov.enc.web.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.egov.tracer.model.CustomException;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import javax.validation.constraints.*;
    import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;
        import java.util.Optional;
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-10-11T17:31:52.360+05:30")

@Controller
public class CryptoApiController{

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private AESEncryptionService aesEncryptionService;

    private RSAEncryptionService rsaEncryptionService;

    private KeyManagementApplication keyManagementApplication;

    @Autowired
    public CryptoApiController(ObjectMapper objectMapper, HttpServletRequest request, AESEncryptionService aesEncryptionService, RSAEncryptionService rsaEncryptionService, KeyManagementApplication keyManagementApplication) {
        this.objectMapper = objectMapper;
        this.request = request;

        this.aesEncryptionService = aesEncryptionService;
        this.rsaEncryptionService = rsaEncryptionService;
        this.keyManagementApplication = keyManagementApplication;
    }

    @RequestMapping(value="/crypto/v1/_decrypt", method = RequestMethod.POST)
    public ResponseEntity<CryptObject> cryptoV1DecryptPost(@Valid @RequestBody CryptObject cryptObject) {
        try {
            if(!keyManagementApplication.checkTenant(cryptObject.getTenantId())) {
                throw new CustomException("Tenant Does Not Exist", "Tenant Does Not Exist");
            }
            if(cryptObject.getMethod().equals(CryptObject.MethodEnum.AES)) {
                return new ResponseEntity<>(aesEncryptionService.processJSON(cryptObject, aesEncryptionService::decrypt), HttpStatus.OK );
            } else if(cryptObject.getMethod().equals(CryptObject.MethodEnum.RSA)) {
                return new ResponseEntity<>(rsaEncryptionService.processJSON(cryptObject, rsaEncryptionService::decrypt), HttpStatus.OK );
            } else {
                throw new CustomException("Unknown Encryption Method", "Unknown Encryption Method");
            }
        } catch (Exception e) {
            return new ResponseEntity<CryptObject>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/crypto/v1/_encrypt", method = RequestMethod.POST)
    public ResponseEntity<CryptObject> cryptoV1EncryptPost(@Valid @RequestBody CryptObject cryptObject) {
        try {
            if(!keyManagementApplication.checkTenant(cryptObject.getTenantId())) {
                throw new CustomException("Tenant Does Not Exist", "Tenant Does Not Exist");
            }
            if(cryptObject.getMethod().equals(CryptObject.MethodEnum.AES)) {
                return new ResponseEntity<>(aesEncryptionService.processJSON(cryptObject, aesEncryptionService::encrypt), HttpStatus.OK );
            } else if(cryptObject.getMethod().equals(CryptObject.MethodEnum.RSA)) {
                return new ResponseEntity<>(rsaEncryptionService.processJSON(cryptObject, rsaEncryptionService::encrypt), HttpStatus.OK );
            } else {
                throw new CustomException("Unknown Encryption Method", "Unknown Encryption Method");
            }
        } catch (Exception e) {
            return new ResponseEntity<CryptObject>(HttpStatus.INTERNAL_SERVER_ERROR);
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
