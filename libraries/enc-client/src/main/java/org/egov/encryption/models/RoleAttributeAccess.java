package org.egov.encryption.models;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleAttributeAccess {

    private Role role;
    private List<AttributeAccess> attributeAccessList;

}