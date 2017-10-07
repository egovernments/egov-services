package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private Notice notice;
}
