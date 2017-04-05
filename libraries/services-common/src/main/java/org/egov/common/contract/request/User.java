package org.egov.common.contract.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;

    private String userName;

    private String name;

    private String type;

    private String mobileNumber;

    private String emailId;

    private List<Role> roles;
}

