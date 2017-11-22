package org.egov.swm.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Department {

    @JsonProperty("id")
    private Long id = null;

    @NotNull

    @Size(min = 1, max = 64)
    @JsonProperty("name")
    private String name = null;

    @NotNull

    @Size(min = 1, max = 10)
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @JsonProperty("active")
    private Boolean active = null;

    @NotNull
    @Size(min = 1, max = 128)
    @JsonProperty("tenantId")
    private String tenantId = null;

}
