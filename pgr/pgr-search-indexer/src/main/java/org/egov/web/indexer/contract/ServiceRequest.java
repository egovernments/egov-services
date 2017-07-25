package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ServiceRequest {
    private static final String CITIZEN_USER_ID = "systemCitizenUserId";

    @JsonProperty("serviceRequestId")
    private String crn;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("statusNotes")
    private String statusDetails;

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("serviceCode")
    private String serviceCode;

    @JsonProperty("description")
    private String details;

    @JsonProperty("requestedDatetime")
    private String createdDate;

    @JsonProperty("expectedDatetime")
    private String escalationDate;

    @JsonProperty("updatedDatetime")
    private String lastModifiedDate;

    @JsonProperty("address")
    private String landmarkDetails;

    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lng")
    private Double lng;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tenantId")
    private String tenantId;

    private List<AttributeEntry> attribValues;

    public String getDynamicSingleValue(String key) {
        return AttributeValues.getAttributeSingleValue(attribValues, key);
    }

    public Long getCitizenUserId() {
        final String userId = getDynamicSingleValue(CITIZEN_USER_ID);
        return isNotEmpty(userId) ? Long.valueOf(userId) : null;
    }
}