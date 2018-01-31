package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class RefillingPumpStation {

    @Length(min = 1, max = 256, message = "Value of code shall be between 1 and 128")
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("location")
    private Boundary location = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of name shall be between 1 and 256")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @JsonProperty("typeOfPump")
    private OilCompany typeOfPump = null;

    @Length(min = 0, max = 300, message = "Value of remarks shall be between 0 and 300")
    @JsonProperty("remarks")
    private String remarks = null;

    @NotNull
    @Size(min = 1)
    @JsonProperty("fuelTypes")
    private List<FuelType> fuelTypes = null;

    @JsonProperty("quantity")
    @Max(value = 10000, message = "Value of Quantity shall be between 0 and 10000 litres")
    private Double quantity = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
