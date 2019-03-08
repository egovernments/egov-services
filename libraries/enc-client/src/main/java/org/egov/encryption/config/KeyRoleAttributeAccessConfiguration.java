package org.egov.encryption.config;

import org.egov.encryption.models.KeyRoleAttributeAccess;
import org.egov.encryption.models.RoleAttributeAccess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyRoleAttributeAccessConfiguration {

    @Value("${egov.mdms.host}")
    private String egovMdmsHost;
    @Value("${egov.mdms.search.endpoint}")
    private String egovMdmsSearchEndpoint;

    private Map<String, List<RoleAttributeAccess>> keyRoleAttributeAccessMap;

    public KeyRoleAttributeAccessConfiguration() {
        initializeKeyRoleAttributeAccessMapFromMdms();
    }

    private void initializeKeyRoleAttributeAccessMapFromMdms() {
        RestTemplate restTemplate = new RestTemplate();

    }

    public KeyRoleAttributeAccessConfiguration(List<KeyRoleAttributeAccess> keyRoleAttributeAccessList) {
        keyRoleAttributeAccessMap = new HashMap<>();
        keyRoleAttributeAccessMap = keyRoleAttributeAccessList.stream()
                        .collect(Collectors.toMap(KeyRoleAttributeAccess::getKeyId,
                                KeyRoleAttributeAccess::getRoleAttributeAccessList));
    }

    public List<RoleAttributeAccess> getRoleAttributeAccessListForKey(String keyId) {
        return keyRoleAttributeAccessMap.get(keyId);
    }
}
