package org.egov.pt.calculator.web.models.property;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Address
 */
@Validated

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address   {
        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("longitude")
        private Double longitude;

        @JsonProperty("addressId")
        private String addressId;

        @JsonProperty("addressNumber")
        private String addressNumber;

        @JsonProperty("type")
        private String type;

        @JsonProperty("addressLine1")
        private String addressLine1;

        @JsonProperty("addressLine2")
        private String addressLine2;

        @JsonProperty("landmark")
        private String landmark;

        @JsonProperty("city")
        private String city;

        @JsonProperty("pincode")
        private String pincode;

        @JsonProperty("detail")
        private String detail;

        @JsonProperty("buildingName")
        private String buildingName;

        @JsonProperty("street")
        private String street;

        @JsonProperty("locality")
        private Boundary locality;


}

