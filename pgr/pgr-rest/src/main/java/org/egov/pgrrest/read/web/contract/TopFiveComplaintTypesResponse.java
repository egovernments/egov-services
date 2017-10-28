package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TopFiveComplaintTypesResponse {
    private List<ComplaintTypeLegend> legends;

    @JsonProperty("data")
    private List<TopComplaintTypesResponse> complaintTypes;
}
