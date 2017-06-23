package org.egov.pgr.location.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @JsonProperty("addressId")
    private String crossHierarchyId;

    public boolean isCrossHierarchyIdPresent() {
        return isNotEmpty(this.crossHierarchyId);
    }

    @JsonProperty("lat")
    private Double lat;
    
    
    @JsonProperty("tenantId")
    private String tenantId;

    public String getLatitude() {
        return lat.toString();
    }

    @JsonProperty("lng")
    private Double lng;

    public String getLongitude() {
        return lng.toString();
    }

    public boolean isLocationCoordinatesPresent() {
        return this.getLat() != null && this.getLat() > 0 && this.getLng() != null && this.getLng() > 0;
    }

}