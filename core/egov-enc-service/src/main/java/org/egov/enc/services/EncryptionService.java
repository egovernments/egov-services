package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.models.Ciphertext;
import org.egov.enc.models.Plaintext;
import org.egov.enc.web.models.CryptObject;

import java.util.*;
import java.util.function.BiFunction;


@Slf4j
public abstract class EncryptionService {

    public abstract Ciphertext encrypt(Plaintext plaintext) throws Exception ;

    public abstract Plaintext decrypt(Ciphertext ciphertext) throws Exception;

    public String encrypt(String plain, String tenantId) {
        try {
            return encrypt(new Plaintext(tenantId, plain)).toString();
        } catch (Exception e) {
            log.error(e.toString());
            return "Runtime Exception";
        }
    }

    public String decrypt(String cipher, String tenantId) {
        try {
            return decrypt(new Ciphertext(cipher)).toString();
        } catch (Exception e) {
            log.error(e.toString());
            return "Runtime Exception";
        }
    }

    public CryptObject processJSON(CryptObject cryptObject, BiFunction<String, String, String> crypt) throws Exception {

        CryptObject outputCryptObject = new CryptObject();
        outputCryptObject.setTenantId(cryptObject.getTenantId());
        outputCryptObject.setMethod(cryptObject.getMethod());
        if(cryptObject.getValue() instanceof String) {
            outputCryptObject.setValue(crypt.apply((String) cryptObject.getValue(), cryptObject.getTenantId()));
        } else if(cryptObject.getValue() instanceof Map) {
            outputCryptObject.setValue(processJSONObject((Map) cryptObject.getValue(), cryptObject.getTenantId(), crypt));
        } else if(cryptObject.getValue() instanceof List) {
            outputCryptObject.setValue(processJSONArray((List) cryptObject.getValue(), cryptObject.getTenantId(), crypt));
        }
        return outputCryptObject;
    }


    public Map processJSONObject(Map jsonObject, String tenantId, BiFunction<String, String, String> crypt) throws Exception {
        HashMap outputJSONObject = new HashMap();
        Set<String> keySet = jsonObject.keySet();
        Iterator<String> keyNames = keySet.iterator();
        for(int i = 0; keyNames.hasNext(); i++) {
            String key = keyNames.next();
            if(jsonObject.get(key) instanceof String)  {
                outputJSONObject.put(key, crypt.apply((String) jsonObject.get(key), tenantId));
            } else if(jsonObject.get(key) instanceof List) {
                outputJSONObject.put(key, processJSONArray((List) jsonObject.get(key), tenantId, crypt));
            } else if(jsonObject.get(key) instanceof Map) {
                outputJSONObject.put(key, processJSONObject((Map) jsonObject.get(key), tenantId, crypt));
            }
        }
        return outputJSONObject;
    }

    private List processJSONArray(List jsonArray, String tenantId, BiFunction<String, String, String> crypt) throws Exception {
        LinkedList outputArray = new LinkedList();
        for(int i = 0; i < jsonArray.size(); i++) {
            if(jsonArray.get(i) instanceof String) {
                outputArray.add(i, crypt.apply((String) jsonArray.get(i), tenantId));
            } else if(jsonArray.get(i) instanceof List) {
                outputArray.add(i, processJSONArray((List) jsonArray.get(i), tenantId, crypt));
            } else if(jsonArray.get(i) instanceof Map) {
                outputArray.add(i, processJSONObject((Map) jsonArray.get(i), tenantId, crypt));
            }
        }
        return outputArray;
    }

}
