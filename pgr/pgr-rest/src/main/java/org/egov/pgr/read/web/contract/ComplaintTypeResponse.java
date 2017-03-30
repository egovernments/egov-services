package org.egov.pgr.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ComplaintTypeResponse {
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    @JsonProperty("ComplaintType")
    private ComplaintType complaintType;

    public ComplaintTypeResponse(org.egov.pgr.read.persistence.entity.ComplaintType complaintType) {
        this.complaintType = new ComplaintType(complaintType);

    }
}
