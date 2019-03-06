package org.egov.encryption.accesscontrol;

import org.egov.encryption.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbacFilter {

    private Map<Role, List<AttributeAccess>> roleAttributeAccessMapping;

    private void initializeRoleAttributeAccessMapping(List<RoleAttributeAccess> roleAttributeAccessList) {
        roleAttributeAccessMapping = new HashMap<>();
        for(RoleAttributeAccess roleAttributeAccess : roleAttributeAccessList) {
            roleAttributeAccessMapping.put(roleAttributeAccess.getRole(), roleAttributeAccess.getAttributeAccessList());
        }
    }

    public AbacFilter(List<RoleAttributeAccess> roleAttributeAccessList) {
        initializeRoleAttributeAccessMapping(roleAttributeAccessList);
    }

    public Map<Attribute, AccessType> getAttributeAccessForRoles(List<Role> roles) {

        Map<Attribute, AccessType> attributeAccessTypeMap = new HashMap<>();

        for(Role role : roles) {
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