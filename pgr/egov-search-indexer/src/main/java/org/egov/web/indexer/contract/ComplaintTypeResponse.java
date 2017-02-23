package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ComplaintTypeResponse {

    @JsonProperty("ComplaintType")
    private ComplaintType complaintType;

}