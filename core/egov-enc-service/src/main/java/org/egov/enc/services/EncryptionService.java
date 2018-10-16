package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.ModeEnum;
import org.egov.enc.models.Plaintext;
import org.egov.enc.utils.ProcessJSONUtil;
import org.egov.enc.web.models.EncryptReqObject;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EncryptionService {


    private ProcessJSONUtil processJSONUtil;


    @Autowired
    public EncryptionService(ProcessJSONUtil processJSONUtil) {
        this.processJSONUtil = processJSONUtil;
    }

    public Object encrypt(Object encryptReq) throws Exception {
        if(encryptReq instanceof EncryptReqObject) {
            EncryptReqObject encryptReqObject = (EncryptReqObject) encryptReq;
            return processJSONUtil.processJSON(encryptReqObject.getValue(), ModeEnum.ENCRYPT, encryptReqObject.getMethod(), encryptReqObject.getTenantId());
        } else if(encryptReq instanceof List) {
            LinkedList<EncryptReqObject> encryptReqObjectLinkedList = (LinkedList<EncryptReqObject>) ((List) encryptReq).stream().collect(Collectors.toCollection(LinkedList::new));
            LinkedList<Object> outputList = new LinkedList<>();
            for(EncryptReqObject encryptReqObject: encryptReqObjectLinkedList) {
                outputList.add(processJSONUtil.processJSON(encryptReqObject.getValue(), ModeEnum.ENCRYPT, encryptReqObject.getMethod(), encryptReqObject.getTenantId()));
            }
            return outputList;
        } else {
            throw new CustomException("Input Object Not Recognized", "Invalid Input");
        }
    }

    public Object decrypt(Object decryptReq) throws Exception {
        return processJSONUtil.processJSON(decryptReq, ModeEnum.DECRYPT, null, null);
    }
}
