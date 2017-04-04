package org.egov.eis.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo {

    @NotNull
    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private String mobileNumber;

    @NotNull
    private String emailId;

    @NotNull
    private List<Role> roles;
    
    @NotNull
    private String tenantId;
}
