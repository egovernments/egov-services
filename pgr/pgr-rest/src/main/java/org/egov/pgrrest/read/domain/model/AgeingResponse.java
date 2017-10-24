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

    private String lessThan15;

    private String lessThan45;

    private String lessThan90;

    private String greaterThan90;

    public org.egov.pgrrest.read.web.contract.AgeingResponse toContract(){
        return org.egov.pgrrest.read.web.contract.AgeingResponse.builder()
            .lessThan15(lessThan15)
            .lessThan45(lessThan45)
            .lessThan90(lessThan90)
            .greaterThan90(greaterThan90)
            .build();
    }
}
