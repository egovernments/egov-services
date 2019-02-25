package org.egov.encryption.accesscontrol;

import org.egov.common.contract.request.Role;
import org.egov.encryption.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbacFilter {

    private List<RoleAttribute> roleAttributesMapping;

    public Map<Attribute, AccessType> getAttributeAccessForRole(List<Role> roles) {

        Map<Attribute, AccessType> attributeAccessTypeMap = new HashMap<Attribute, AccessType>();

        for(RoleAttribute roleAttribute : roleAttributesMapping) {
            if(roles.contains(roleAttribute.getRole())) {
                List<Attribute> attributes = roleAttribute.getAttributes();
                for(Attribute attribute : attributes) {
                    if(attributeAccessTypeMap.containsKey(attribute)) {
                        if(roleAttribute.getAccessType().compareTo(attributeAccessTypeMap.get(attribute)) < 0) {
                            attributeAccessTypeMap.put(attribute, roleAttribute.getAccessType());
                        }
                    } else {
                        attributeAccessTypeMap.put(attribute, roleAttribute.getAccessType());
                    }
                }
            }
        }

        return attributeAccessTypeMap;
    }


}
