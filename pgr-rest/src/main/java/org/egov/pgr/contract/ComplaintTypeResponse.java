package org.egov.pgr.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.pgr.model.ResponseInfo;

@Getter
public class ComplaintTypeResponse {
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    @JsonProperty("ComplaintType")
    private ComplaintType complaintType;

    public ComplaintTypeResponse(org.egov.pgr.entity.ComplaintType complaintType) {
        this.complaintType = new ComplaintType(complaintType);

    }
}
