package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TopFiveComplaintTypesResponse {

    private List<ComplaintTypeLegend> legends;

    private List<TopComplaintTypesResponse> complaintTypes;
}
