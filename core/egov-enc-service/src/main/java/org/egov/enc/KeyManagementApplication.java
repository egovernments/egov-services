package org.egov.enc;

import org.egov.enc.keymanagement.KeyGenerator;
import org.egov.enc.keymanagement.KeyStore;
import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.SymmetricKey;
import org.egov.enc.repository.KeyRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class KeyManagementApplication implements ApplicationRunner {

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private KeyStore keyStore;

    private ArrayList<String> tenantIdsFromDB;

    //Initialize active tenant id list
    public void init() {
        tenantIdsFromDB = (ArrayList<String>) this.keyRepository.fetchDistinctTenantIds();
    }

    //Check if a given tenantId exists
    public boolean checkIfTenantExists(String tenant) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        if(tenantIdsFromDB.contains(tenant)) {
            return true;
        }
        if(generateKeyForNewTenants() != 0) {
            keyStore.refreshKeys();
            tenantIdsFromDB = (ArrayList<String>) keyRepository.fetchDistinctTenantIds();
            if(tenantIdsFromDB.contains(tenant)) {
                return true;
            }
        }
        return false;
    }

    //Generate Symmetric and Asymmetric Keys for each of the TenantId in the given input list
    public void generateKeys(ArrayList<String> tenantIds) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {

        ArrayList<SymmetricKey> symmetricKeys = keyGenerator.generateSymmetricKeys(tenantIds);
        for(SymmetricKey symmetricKey : symmetricKeys) {
            keyRepository.insertSymmetricKey(symmetricKey);
        }

        ArrayList<AsymmetricKey> asymmetricKeys = keyGenerator.generateAsymmetricKeys(tenantIds);
        for(AsymmetricKey asymmetricKey : asymmetricKeys) {
            keyRepository.insertAsymmetricKey(asymmetricKey);
        }
    }

    //Generate keys if there are any new tenants
    //Returns the number of tenants for which the keys have been generated
    public int generateKeyForNewTenants() throws JSONException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        Collection<String> tenantIds = getTenantIds();
        Collection<String> tenantIdsFromDB = keyRepository.fetchDistinctTenantIds();

        tenantIds.removeAll(tenantIdsFromDB);

        generateKeys((ArrayList<String>) tenantIds);

        return tenantIds.size();
    }

    //Used to deactivate old keys at the time of key rotation
    public void deactivateOldKeys() {
        keyRepository.deactivateSymmetricKeys();
        keyRepository.deactivateAsymmetricKeys();
    }

    //Deactivate old keys and generate new keys for every tenantId
    public void rotateKeys() throws JSONException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        deactivateOldKeys();
        generateKeys(getTenantIds());
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

        String url = "https://egov-micro-dev.egovernments.org/egov-mdms-service/v1/_search";

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
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
