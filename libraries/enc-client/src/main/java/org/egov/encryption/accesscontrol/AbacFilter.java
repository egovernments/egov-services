package org.egov.encryption.accesscontrol;

import org.egov.common.contract.request.Role;
import org.egov.encryption.models.AccessType;
import org.egov.encryption.models.Attribute;
import org.egov.encryption.models.RoleAttributeAccess;

import java.util.List;
import java.util.Map;

public class AbacFilter {

    private List<RoleAttributeAccess> roleAttributeAccessMapping;

    public AbacFilter(List<RoleAttributeAccess> roleAttributeAccessMapping) {
        this.roleAttributeAccessMapping = roleAttributeAccessMapping;
    }

    public Map<Attribute, AccessType> getAttributeAccessForRoles(List<Role> roles) {

//        Map<Attribute, AccessType> attributeAccessTypeMap = new HashMap<Attribute, AccessType>();
//
//        for(RoleAttributeAccess roleAttribute : roleAttributeAccessMapping) {
//            if(roles.contains(roleAttribute.getRole())) {
//                List<Attribute> attributes = roleAttribute.getAttributes();
//                for(Attribute attribute : attributes) {
//                    if(attributeAccessTypeMap.containsKey(attribute)) {
//                        if(roleAttribute.getAccessType().compareTo(attributeAccessTypeMap.get(attribute)) < 0) {
//                            attributeAccessTypeMap.put(attribute, roleAttribute.getAccessType());
//                        }
//                    } else {
//                        attributeAccessTypeMap.put(attribute, roleAttribute.getAccessType());
//                    }
//                }
//            }
//        }
//
//        return attributeAccessTypeMap;
        return null;
    }

}