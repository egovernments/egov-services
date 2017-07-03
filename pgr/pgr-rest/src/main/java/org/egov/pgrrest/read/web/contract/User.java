package org.egov.pgrrest.read.web.contract;

import lombok.*;
import org.egov.pgrrest.common.domain.model.Role;

import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String userName;

    private String name;

    private String mobileNumber;

    private String emailId;

    private String type;

    private String permanentAddress;

    private String tenantId;

    private Set<Role> roles = null;
}
