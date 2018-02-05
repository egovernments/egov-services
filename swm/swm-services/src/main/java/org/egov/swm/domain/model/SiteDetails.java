package org.egov.swm.domain.model;

import java.util.List;

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
public class SiteDetails {

    @JsonProperty("code")
    private String code;

    @JsonProperty("location")
    @NotNull
    private Boundary location;

    @JsonProperty("area")
    @NotNull
    private Double area;

    @JsonProperty("capacity")
    @NotNull
    private Double capacity;

    @JsonProperty("address")
    @Length(min = 15, max = 500, message = "Value of address shall be between 15 and 500")
    @NotNull
    private String address;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("wasteTypes")
    @NotNull
    private List<WasteType> wasteTypes;

    @JsonProperty("mpcbAuthorisation")
    private Boolean mpcbAuthorisation;

    @JsonProperty("bankGuarantee")
    private Boolean bankGuarantee;

    @JsonProperty("bankName")
    @Length(min = 0, max = 256, message = "Value of bankName shall be between 0 and 256")
    private String bankName;

    @JsonProperty("bankValidityFrom")
    private Long bankValidityFrom;

    @JsonProperty("bankValidityTo")
    private Long bankValidityTo;

}