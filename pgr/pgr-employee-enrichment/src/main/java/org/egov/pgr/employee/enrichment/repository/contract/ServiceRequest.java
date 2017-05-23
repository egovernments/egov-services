package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ServiceRequest {

    @JsonProperty("serviceCode")
    private String complaintTypeCode;

    @JsonProperty("description")
    private String description;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    public String getDynamicSingleValue(String key) {
        return AttributeValues.getAttributeSingleValue(attribValues, key);
    }
}
