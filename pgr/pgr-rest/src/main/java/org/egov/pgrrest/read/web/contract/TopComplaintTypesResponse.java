package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TopComplaintTypesResponse {

    private Integer count;

    @JsonProperty("ComplaintType")
    private String complaintTypeName;

    private Integer month;

    private String code;

    private Integer boundary;

    private String boundaryName;
}
