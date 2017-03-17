package org.egov.user.web.contract.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class Role implements Serializable {
    private static final long serialVersionUID = 2090518436085399889L;
    private String name;

    public Role(org.egov.user.domain.model.Role role) {
        this.name = role.getName();
    }
}
