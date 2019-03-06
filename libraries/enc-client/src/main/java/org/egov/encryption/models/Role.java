package org.egov.encryption.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Role {

    private String code;

    public Role(org.egov.common.contract.request.Role role) {
        this.code = role.getCode();
    }

}
