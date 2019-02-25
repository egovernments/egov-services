package org.egov.encryption.models;

import lombok.*;
import org.egov.common.contract.request.Role;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleAttribute {

    private Role role;
    private List<Attribute> attributes;
    private AccessType accessType;

}
