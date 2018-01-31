package org.egov.swm.web.contract;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class Designation {

    @NotNull
    private Long id;

    @NotNull
    @Length(min = 3, max = 100)
    private String name;

    @NotNull
    @Length(min = 3, max = 20)
    private String code;

    @Length(max = 250)
    private String description;

    private String chartOfAccounts;

    @NotNull
    private Boolean active;

    @NotNull
    private String tenantId;

}
