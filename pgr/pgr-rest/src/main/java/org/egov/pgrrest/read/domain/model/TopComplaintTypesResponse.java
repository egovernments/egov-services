package org.egov.pgrrest.read.domain.model;

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

    private String complaintTypeName;

    private Integer month;

    private String code;

    private Integer boundary;

    private String boundaryName;

    public org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse toContract(){
        return org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse.builder()
               .count(count)
               .complaintTypeName(complaintTypeName)
               .code(code)
               .build();
    }

    public org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse toTopFiveComplaintTypesContract(){
        return org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse.builder()
            .count(count)
            .complaintTypeName(complaintTypeName)
            .month(month)
            .code(code)
            .build();
    }

    public org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse toWardWiseContract(){
        return org.egov.pgrrest.read.web.contract.TopComplaintTypesResponse.builder()
            .count(count)
            .boundary(boundary)
            .boundaryName(boundaryName)
            .build();
    }
}
