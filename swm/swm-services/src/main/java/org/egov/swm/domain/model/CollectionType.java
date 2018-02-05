package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionType {

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of name shall be between 1 and 256")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @Length(min = 1, max = 128, message = "Value of code shall be between 1 and 128")
    @JsonProperty("code")
    private String code = null;

}
