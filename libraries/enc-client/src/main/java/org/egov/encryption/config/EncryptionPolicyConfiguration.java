package org.egov.encryption.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.EncryptionPolicy;
import org.egov.mdms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EncryptionPolicyConfiguration {

    @Autowired
    private EncProperties encProperties;

    private Map<String, List<Attribute>> keyAttributeMap;

    @PostConstruct
    private void initializeKeyAttributeMapFromMdms() {
        List<EncryptionPolicy> encryptionPolicyList = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
            String mdmsRequest = "{\"RequestInfo\":{},\"MdmsCriteria\":{\"tenantId\":\"" + encProperties.getStateLevelTenantId() + "\"," +
                    "\"moduleDetails\":[{\"moduleName\":\"DataSecurity\"," +
                    "\"masterDetails\":[{\"name\":\"EncryptionPolicy\"}]}]}}";

            MasterDetail masterDetail = MasterDetail.builder().name(EncClientConstants.MDMS_ENCRYPTION_MASTER_NAME).build();
            ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(EncClientConstants.MDMS_MODULE_NAME)
                    .masterDetails(Arrays.asList(masterDetail)) .build();

            MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(encProperties.getStateLevelTenantId())
                    .moduleDetails(Arrays.asList(moduleDetail)).build();

            MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(RequestInfo.builder().build())
                    .mdmsCriteria(mdmsCriteria).build();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MdmsResponse> response =
                    restTemplate.postForEntity(encProperties.getEgovMdmsHost() + encProperties.getEgovMdmsSearchEndpoint(),
                            mdmsCriteriaReq, MdmsResponse.class);

            JSONArray policyListJSON = response.getBody().getMdmsRes().get(EncClientConstants.MDMS_MODULE_NAME)
                    .get(EncClientConstants.MDMS_DECRYPTION_MASTER_NAME);

            ObjectReader reader = objectMapper.readerFor(objectMapper.getTypeFactory().constructCollectionType(List.class,
                    EncryptionPolicy.class));
            encryptionPolicyList = reader.readValue(policyListJSON.toString());
        } catch (IOException e) {}

        initializeKeyAttributeMap(encryptionPolicyList);
    }

    private void initializeKeyAttributeMap(List<EncryptionPolicy> encryptionPolicyList) {
        keyAttributeMap = encryptionPolicyList.stream().collect(Collectors
                        .toMap(EncryptionPolicy::getKey, EncryptionPolicy::getAttributeList));
    }

    public List<Attribute> getAttributesForKey(String key) {
        return keyAttributeMap.get(key);
    }

    public Map<String, List<Attribute>> getTypeAttributeMap(List<Attribute> attributeList) {
        Map<String, List<Attribute>> typeAttributeMap = new HashMap<>();
        for (Attribute attribute : attributeList) {
            String type = attribute.getType();
            if (! typeAttributeMap.containsKey(type)) {
                List<Attribute> attributesToEncrypt = new ArrayList<>();
                typeAttributeMap.put(type, attributesToEncrypt);
            }
            typeAttributeMap.get(type).add(attribute);
        }
        return typeAttributeMap;
    }
}
