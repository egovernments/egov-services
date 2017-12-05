package org.egov.swm.domain.model;

import java.math.BigDecimal;

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
public class City {

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("localName")
    private String localName = null;

    @JsonProperty("districtCode")
    private String districtCode = null;

    @JsonProperty("districtName")
    private String districtName = null;

    @JsonProperty("regionName")
    private String regionName = null;

    @JsonProperty("longitude")
    private BigDecimal longitude = null;

    @JsonProperty("latitude")
    private BigDecimal latitude = null;

    @Length(max = 100)
    @JsonProperty("shapeFilePath")
    private String shapeFilePath = null;

    @Length(max = 100)
    @JsonProperty("captcha")
    private String captcha = null;

}
