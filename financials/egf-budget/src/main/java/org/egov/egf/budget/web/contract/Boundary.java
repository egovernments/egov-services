package org.egov.egf.budget.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boundary {

    private String id;
    private String name;
    private Float longitude;
    private Float latitude;
    private Long boundaryNum;
    private String tenantId;
}
