package org.egov.enc.utils;

import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.MethodEnum;
import org.egov.enc.models.ModeEnum;
import org.egov.enc.models.Plaintext;
import org.egov.enc.services.AESEncryptionService;
import org.egov.enc.services.RSAEncryptionService;
import org.egov.enc.web.models.EncryptReqObject;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.function.BiFunction;

@Component
public class ProcessJSONUtil {

    private AESEncryptionService aesEncryptionService;
    private RSAEncryptionService rsaEncryptionService;

    @Autowired
    public ProcessJSONUtil(AESEncryptionService aesEncryptionService, RSAEncryptionService rsaEncryptionService) {
        this.aesEncryptionService = aesEncryptionService;
        this.rsaEncryptionService = rsaEncryptionService;
    }

    public Object processJSON(Object inputObject, ModeEnum mode, MethodEnum method, String tenantId) throws Exception {
        Object outputObject;

        if(inputObject instanceof Map) {
            outputObject = processJSONObject((Map) inputObject, mode, method, tenantId);
        } else if(inputObject instanceof List) {
            outputObject = processJSONArray((List) inputObject, mode, method, tenantId);
        } else {
            outputObject = processValue(inputObject, mode, method, tenantId);
        }
        return outputObject;
    }

    private Map processJSONObject(Map jsonObject, ModeEnum mode, MethodEnum method, String tenantId) throws Exception {
        HashMap outputJSONObject = new HashMap();
        Set<String> keySet = jsonObject.keySet();
        Iterator<String> keyNames = keySet.iterator();
        for(int i = 0; keyNames.hasNext(); i++) {
            String key = keyNames.next();
            if(jsonObject.get(key) instanceof List) {
                outputJSONObject.put(key, processJSONArray((List) jsonObject.get(key), mode, method, tenantId));
            } else if(jsonObject.get(key) instanceof Map) {
                outputJSONObject.put(key, processJSONObject((Map) jsonObject.get(key), mode, method, tenantId));
            } else {
                outputJSONObject.put(key, processValue(jsonObject.get(key), mode, method, tenantId));
            }
        }
        return outputJSONObject;
    }

    private List processJSONArray(List jsonArray, ModeEnum mode, MethodEnum method, String tenantId) throws Exception {
        LinkedList outputArray = new LinkedList();
        for(int i = 0; i < jsonArray.size(); i++) {
            if(jsonArray.get(i) instanceof List) {
                outputArray.add(i, processJSONArray((List) jsonArray.get(i), mode, method, tenantId));
            } else if(jsonArray.get(i) instanceof Map) {
                outputArray.add(i, processJSONObject((Map) jsonArray.get(i), mode, method, tenantId));
            } else {
                outputArray.add(i, processValue(jsonArray.get(i), mode, method, tenantId));
            }
        }
        return outputArray;
    }

    private String processValue(Object value, ModeEnum mode, MethodEnum method, String tenantId) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if(mode.equals(ModeEnum.ENCRYPT)) {
            Ciphertext ciphertext;
            Plaintext plaintext = new Plaintext(tenantId, value.toString());
            if(method.equals(MethodEnum.AES)) {
                ciphertext = aesEncryptionService.encrypt(plaintext);
            } else {
                ciphertext = rsaEncryptionService.encrypt(plaintext);
            }
            return ciphertext.toString();
        }
        else {
            Plaintext plaintext;
            Ciphertext ciphertext = new Ciphertext(value.toString());
            if(ciphertext.getMethod().equals(MethodEnum.AES)) {
                plaintext = aesEncryptionService.decrypt(ciphertext);
            } else {
                plaintext = rsaEncryptionService.decrypt(ciphertext);
            }
            return plaintext.toString();
        }
    }

}
