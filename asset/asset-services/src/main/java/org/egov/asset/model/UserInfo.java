package org.egov.asset.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -6099520777478122089L;

    private Long id;

    private String userName;

    private String name;

    private String type;

    private String mobileNumber;

    private String emailId;

    private List<RoleInfo> roles;

    private String tenantId;


}