package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @Valid
    private Notice notice;
}
