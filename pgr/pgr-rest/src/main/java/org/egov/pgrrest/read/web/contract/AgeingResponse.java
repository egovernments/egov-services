package org.egov.pgrrest.read.web.contract;

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
}
