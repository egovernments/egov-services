package org.egov.pg.web.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String userName;

    @NotNull
    private String mobileNumber;

    private String emailId;

    @NotNull
    private String tenantId;

}
