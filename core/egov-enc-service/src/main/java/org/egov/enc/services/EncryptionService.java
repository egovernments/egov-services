package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.KeyManagementApplication;
import org.egov.enc.models.ModeEnum;
import org.egov.enc.utils.ProcessJSONUtil;
import org.egov.enc.web.models.EncryptionRequestObject;
import org.egov.enc.web.models.EncryptionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class EncryptionService {

    private ProcessJSONUtil processJSONUtil;
    private KeyManagementApplication keyManagementApplication;

    @Autowired
    public EncryptionService(ProcessJSONUtil processJSONUtil, KeyManagementApplication keyManagementApplication) {
        this.processJSONUtil = processJSONUtil;
        this.keyManagementApplication = keyManagementApplication;
    }

    public Object encrypt(EncryptionRequest encryptionRequest) throws Exception {
        LinkedList<Object> outputList = new LinkedList<>();
        for(EncryptionRequestObject encryptionRequestObject : encryptionRequest.getEncryptionRequestObjects()) {
            if(!keyManagementApplication.checkTenant(encryptionRequestObject.getTenantId())) {
                throw new CustomException("Tenant Does Not Exist", encryptionRequestObject.getTenantId() + " Tenant Does Not Exist");
            }
            outputList.add(processJSONUtil.processJSON(encryptionRequestObject.getValue(), ModeEnum.ENCRYPT, encryptionRequestObject.getMethod(), encryptionRequestObject.getTenantId()));
        }
        return outputList;
    }

    public Object decrypt(Object decryptReq) throws Exception {
        return processJSONUtil.processJSON(decryptReq, ModeEnum.DECRYPT, null, null);
    }
}
