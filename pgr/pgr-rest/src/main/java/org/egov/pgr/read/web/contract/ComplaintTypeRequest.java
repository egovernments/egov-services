package org.egov.pgr.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.read.persistence.entity.ComplaintType;
import org.egov.pgr.common.contract.RequestInfo;

import javax.validation.Valid;

@Getter
@Setter
public class ComplaintTypeRequest {
    @Valid
    @JsonProperty("request_info")
    private RequestInfo requestInfo;
    @Valid
    @JsonProperty("complaint_type")
    private ComplaintType complaintType;
}
