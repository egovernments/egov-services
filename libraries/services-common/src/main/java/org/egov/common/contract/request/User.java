package org.egov.common.contract.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {
    private Long id;

    private String userName;

    private String name;

    private String type;

    private String mobileNumber;

    private String emailId;

    private List<Role> roles;

    private String tenantId;
    
    private String uuid;
}

