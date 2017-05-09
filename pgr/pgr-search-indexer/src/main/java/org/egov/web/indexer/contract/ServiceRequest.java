package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ServiceRequest {
    private static final String SERVICE_STATUS = "status";
    private static final String REGISTERED = "REGISTERED";

    @JsonProperty("service_request_id")
    private String crn = null;

    @JsonProperty("status")
    private Boolean status = null;

    @JsonProperty("statusNotes")
    private String statusDetails = null;

    @JsonProperty("service_name")
    private String complaintTypeName = null;

    @JsonProperty("service_code")
    private String complaintTypeCode = null;

    @JsonProperty("description")
    private String details = null;

    @JsonProperty("requested_datetime")
    private String createdDate = null;

    @JsonProperty("expected_datetime")
    private String escalationDate = null;

    @JsonProperty("address")
    private String landmarkDetails = null;

    @JsonProperty("lat")
    private Double lat = null;

    @JsonProperty("lng")
    private Double lng = null;

    @JsonProperty("first_name")
    private String firstName = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("values")
    private Map<String, String> values = new HashMap<>();

    //  Short term hack - to support values and attribValues usage
//  This flag should be set by the consumer for the service to consider attribValues instead of existing values field.
    @JsonProperty("isAttribValuesPopulated")
    private boolean attribValuesPopulated;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    public String getDynamicSingleValue(String key) {
        if (attribValuesPopulated) {
            return AttributeValues.getAttributeSingleValue(attribValues, key);
        } else {
            return values.get(key);
        }
    }

    @JsonIgnore
    public boolean isNewServiceRequest() {
        return getDynamicSingleValue(SERVICE_STATUS).equalsIgnoreCase(REGISTERED);
    }
   /* 
    @JsonProperty("agency_responsible")
    private String agencyResponsible = null;

    @JsonProperty("service_notice")
    private String serviceNotice = null;
    
    @JsonProperty("address_id")
    private String crossHierarchyId = null;

    @JsonProperty("zipcode")
    private Integer zipcode = null;
    
    @JsonProperty("media_urls")
    private String mediaUrl = null;
    
    @JsonProperty("device_id")
    private String deviceId = null;

    @JsonProperty("account_id")
    private String accountId = null;
    
    @JsonProperty("last_name")
    private String lastName = null;
    
    @JsonProperty("updated_datetime")
    private String lastModifiedDate = null;*/
}