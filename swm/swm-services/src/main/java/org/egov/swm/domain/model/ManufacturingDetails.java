package org.egov.swm.domain.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class ManufacturingDetails {

    @Length(min = 1, max = 256)
    @JsonProperty("engineSrNumber")
    private String engineSrNumber = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("chassisSrNumber")
    private String chassisSrNumber = null;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @JsonProperty("vehicleCapacity")
    private Double vehicleCapacity = null;

    @NotNull
    @Length(min = 0, max = 256)
    @JsonProperty("model")
    private String model = null;

}
