package org.egov.pgrrest.read.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ComplaintTypeLegend {

    private String complaintTypeName;

    private Integer rank;

    public org.egov.pgrrest.read.web.contract.ComplaintTypeLegend toContract(){
        return org.egov.pgrrest.read.web.contract.ComplaintTypeLegend.builder()
            .complaintTypeName(complaintTypeName)
            .rank(rank)
            .build();
    }
}
