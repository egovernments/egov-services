package org.egov.enc.services;

import lombok.extern.slf4j.Slf4j;
import org.egov.enc.keymanagement.KeyGenerator;
import org.egov.enc.keymanagement.KeyIdGenerator;
import org.egov.enc.keymanagement.KeyStore;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.SymmetricKey;
import org.egov.enc.repository.KeyRepository;
import org.egov.enc.web.models.RotateKeyRequest;
import org.egov.enc.web.models.RotateKeyResponse;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class KeyManagementService implements ApplicationRunner {

    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndpoint;

    @Autowired
    private KeyRepository keyRepository;
    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private KeyStore keyStore;
    @Autowired
    private KeyIdGenerator keyIdGenerator;

    private ArrayList<String> tenantIdsFromDB;

    //Initialize active tenant id list
    private void init() {
        tenantIdsFromDB = (ArrayList<String>) this.keyRepository.fetchDistinctTenantIds();
    }

    //Check if a given tenantId exists
    public boolean checkIfTenantExists(String tenant) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if(tenantIdsFromDB.contains(tenant)) {
            return true;
        }
        int numberOfNewTenants = generateKeyForNewTenants();
        if(numberOfNewTenants != 0) {
            keyStore.refreshKeys();
            keyIdGenerator.refreshKeyIds();
            tenantIdsFromDB = (ArrayList<String>) keyRepository.fetchDistinctTenantIds();
            return tenantIdsFromDB.contains(tenant);
        }
        return false;
    }

    //Generate Symmetric and Asymmetric Keys for each of the TenantId in the given input list
    public void generateKeys(ArrayList<String> tenantIds) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {

        Integer status;
        ArrayList<SymmetricKey> symmetricKeys = keyGenerator.generateSymmetricKeys(tenantIds);
        for(SymmetricKey symmetricKey : symmetricKeys) {
            status = keyRepository.insertSymmetricKey(symmetricKey);
            if(status != 1) {
                throw new CustomException("DB Insert Exception", "DB Insert Exception");
            }
        }

        ArrayList<AsymmetricKey> asymmetricKeys = keyGenerator.generateAsymmetricKeys(tenantIds);
        for(AsymmetricKey asymmetricKey : asymmetricKeys) {
            status = keyRepository.insertAsymmetricKey(asymmetricKey);
            if(status != 1) {
                throw new CustomException("DB Insert Exception", "DB Insert Exception");
            }
        }
    }

    //Generate keys if there are any new tenants
    //Returns the number of tenants for which the keys have been generated
    public int generateKeyForNewTenants() throws JSONException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        Collection<String> tenantIds = makeComprehensiveListOfTenantIds();
        Collection<String> tenantIdsFromDB = keyRepository.fetchDistinctTenantIds();

        tenantIds.removeAll(tenantIdsFromDB);

        ArrayList<String> tenantIdList = new ArrayList<>(tenantIds);

        generateKeys(tenantIdList);

        return tenantIds.size();
    }

    private Set<String> makeComprehensiveListOfTenantIds() {
        ArrayList<String> tenantIds = getTenantIds();
        Set<String> comprehensiveTenantIdsSet = new HashSet<>(tenantIds);

        for (String tenantId: tenantIds) {
            int index = tenantId.indexOf(".");
            while(index > 0) {
                comprehensiveTenantIdsSet.add(tenantId.substring(0, index));
                index = tenantId.indexOf(".", index + 1);
            }
        }

        return comprehensiveTenantIdsSet;
    }

    //Used to deactivate old keys at the time of key rotation
    private void deactivateOldKeys() {
        keyRepository.deactivateSymmetricKeys();
        keyRepository.deactivateAsymmetricKeys();
    }

    //Deactivate old keys and generate new keys for every tenantId
    public RotateKeyResponse rotateAllKeys() throws JSONException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        deactivateOldKeys();
        generateKeyForNewTenants();
        return new RotateKeyResponse(true);
    }

    public RotateKeyResponse rotateKey(RotateKeyRequest rotateKeyRequest) throws BadPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException,
            InvalidAlgorithmParameterException {
        Integer status;
        status = keyRepository.deactivateSymmetricKeyForGivenTenant(rotateKeyRequest.getTenantId());
        log.info("Key Rotate SYM Return Status: " + status);
        if(status != 1) {
            throw new CustomException("DB Exception", "DB Exception");
        }
        status = keyRepository.deactivateAsymmetricKeyForGivenTenant(rotateKeyRequest.getTenantId());
        log.info("Key Rotate ASY Return Status: " + status);
        if(status != 1) {
            throw new CustomException("DB Exception", "DB Exception");
        }

        generateKeyForNewTenants();

        return new RotateKeyResponse(true);
    }



    private ArrayList<String> getTenantIds() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\n" +
                " \"RequestInfo\": {\n" +
                "   \"apiId\": \"asset-services\",\n" +
                "   \"ver\": null,\n" +
                "   \"ts\": null,\n" +
                "   \"action\": null,\n" +
                "   \"did\": null,\n" +
                "   \"key\": null,\n" +
                "   \"msgId\": \"search with from and to values\",\n" +
                "   \"authToken\": \"59854f79-7031-4157-9cb5-21c51cb61981\"\n" +
                " },\n" +
                " \"MdmsCriteria\": {\n" +
                "   \"tenantId\": \"pb\",\n" +
                "   \"moduleDetails\": [\n" +
                "     {\n" +
                "       \"moduleName\": \"tenant\",\n" +
                "       \"masterDetails\": [\n" +
                "         {\n" +
                "           \"name\": \"tenants\",\n" +
                "           \"filter\":\"$.*.code\"\n" +
                "         }\n" +
                "       ]\n" +
                "     }\n" +
                "   ]\n" +
                " }\n" +
                "}";

        String url = mdmsHost + mdmsEndpoint;

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray jsonArray = jsonObject.getJSONObject("MdmsRes").getJSONObject("tenant").getJSONArray("tenants");

        ArrayList<String> tenantIds = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            tenantIds.add(jsonArray.getString(i));
        }

        return tenantIds;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        init();
    }
}
