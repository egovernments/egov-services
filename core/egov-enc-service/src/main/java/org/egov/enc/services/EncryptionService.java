package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.KeyManagementApplication;
import org.egov.enc.config.AppProperties;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.ModeEnum;
import org.egov.enc.utils.Constants;
import org.egov.enc.utils.ProcessJSONUtil;
import org.egov.enc.web.models.EncReqObject;
import org.egov.enc.web.models.EncryptionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


@Slf4j
@Service
public class EncryptionService {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private ProcessJSONUtil processJSONUtil;
    @Autowired
    private KeyManagementApplication keyManagementApplication;

    public Object encrypt(EncryptionRequest encryptionRequest) throws Exception {
        LinkedList<Object> outputList = new LinkedList<>();
        for(EncReqObject encReqObject : encryptionRequest.getEncryptionRequests()) {
            if(!keyManagementApplication.checkIfTenantExists(encReqObject.getTenantId())) {
                throw new CustomException(Constants.TENANT_NOT_FOUND, Constants.TENANT_NOT_FOUND );
            }
            outputList.add(processJSONUtil.processJSON(encReqObject.getValue(), ModeEnum.ENCRYPT,
                    MethodEnum.fromValue(appProperties.getTypeToMethodMap().get(encReqObject.getDataType())), encReqObject.getTenantId()));
        }
        return outputList;
    }

    public Object decrypt(Object decryptionRequest) throws Exception {
        return processJSONUtil.processJSON(decryptionRequest, ModeEnum.DECRYPT, null, null);
    }
}
