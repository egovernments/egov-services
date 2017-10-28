package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AgeingResponse {

    private String interval1;

    private String interval2;

    private String interval3;

    private String interval4;

    private String interval5;

    public org.egov.pgrrest.read.web.contract.AgeingResponse toContract(){
        return org.egov.pgrrest.read.web.contract.AgeingResponse.builder()
            .interval1(interval1)
            .interval2(interval2)
            .interval3(interval3)
            .interval4(interval4)
            .interval5(interval5)
            .build();
    }
}
