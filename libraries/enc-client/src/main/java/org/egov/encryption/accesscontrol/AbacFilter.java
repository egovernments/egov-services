package org.egov.encryption.accesscontrol;

import org.egov.encryption.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbacFilter {

    private Map<String, List<AttributeAccess>> roleAttributeAccessMapping;

    private void initializeRoleAttributeAccessMapping(List<RoleAttributeAccess> roleAttributeAccessList) {
        roleAttributeAccessMapping = new HashMap<>();
        for(RoleAttributeAccess roleAttributeAccess : roleAttributeAccessList) {
            roleAttributeAccessMapping.put(roleAttributeAccess.getRoleCode(),
                    roleAttributeAccess.getAttributeAccessList());
        }
    }

    public AbacFilter() {
        List<RoleAttributeAccess> roleAttributeAccessList = getRoleAttributeAccessList();
        initializeRoleAttributeAccessMapping(roleAttributeAccessList);
    }

    public AbacFilter(List<RoleAttributeAccess> roleAttributeAccessList) {
        initializeRoleAttributeAccessMapping(roleAttributeAccessList);
    }

    private List<RoleAttributeAccess> getRoleAttributeAccessList() {
        return null;
    }

    public Map<Attribute, AccessType> getAttributeAccessForRoles(List<String> roles) {

        Map<Attribute, AccessType> attributeAccessTypeMap = new HashMap<>();

        for(String role : roles) {
            List<AttributeAccess> attributeAccessList = roleAttributeAccessMapping.get(role);
            for(AttributeAccess attributeAccess : attributeAccessList) {
                Attribute attribute = attributeAccess.getAttribute();
                AccessType accessType = attributeAccess.getAccessType();
                if(attributeAccessTypeMap.containsKey(attribute)) {
                    if(attributeAccessTypeMap.get(attribute).ordinal() > accessType.ordinal()) {
                        attributeAccessTypeMap.put(attribute, accessType);
                    }
                } else {
                    attributeAccessTypeMap.put(attribute, accessType);
                }
            }
        }

        return attributeAccessTypeMap;

    }

}