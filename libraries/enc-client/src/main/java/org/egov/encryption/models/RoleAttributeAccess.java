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

    private String roleCode;
    private List<AttributeAccess> attributeAccessList;

}