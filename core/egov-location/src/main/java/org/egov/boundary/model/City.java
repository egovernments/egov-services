package org.egov.boundary.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "city_name",
        "city_code",
        "tenant_id",
        "non_active_modules"
})
public class City {

    @JsonProperty("url")
    private String url;
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("city_code")
    private Integer cityCode;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("non_active_modules")
    private NonActiveModules nonActiveModules;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("city_name")
    public String getCityName() {
        return cityName;
    }

    @JsonProperty("city_name")
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @JsonProperty("city_code")
    public Integer getCityCode() {
        return cityCode;
    }

    @JsonProperty("city_code")
    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    
    @JsonProperty("tenant_id")
    public String getTenantId() {
        return tenantId;
    }
    @JsonProperty("tenant_id")
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @JsonProperty("non_active_modules")
    public NonActiveModules getNonActiveModules() {
        return nonActiveModules;
    }

    @JsonProperty("non_active_modules")
    public void setNonActiveModules(NonActiveModules nonActiveModules) {
        this.nonActiveModules = nonActiveModules;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
