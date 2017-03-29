package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComplaintTypeResponse {
    @JsonProperty("ComplaintType")
    private ComplaintType complaintType;

    public Long getId() {
        return complaintType.getId();
    }
}
