package org.egov.pgrrest.read.web.contract;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ComplaintTypeLegend {

    private String complaintTypeName;

    private Integer rank;
}
