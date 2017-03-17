package org.egov.user.web.contract.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Role {
    private String name;

    public Role(org.egov.user.domain.model.Role role) {
        this.name = role.getName();
    }
}
