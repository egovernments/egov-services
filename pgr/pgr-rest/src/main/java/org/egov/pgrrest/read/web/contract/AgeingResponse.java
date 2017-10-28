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

    private String interval1;

    private String interval2;

    private String interval3;

    private String interval4;

    private String interval5;
}
