package org.egov.pgrrest.read.web.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DashboardResponse {

    @JsonProperty("REGISTERED")
    private Integer count;

    @JsonProperty("RESOLVED")
    private Integer closedCount;

    private String name;
}
