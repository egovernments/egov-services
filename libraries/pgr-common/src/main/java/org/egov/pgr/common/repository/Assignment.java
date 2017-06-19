package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Assignment {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("department")
    private Long department;
    
    @JsonProperty("tenantId")
    private String tenantId;

    private Long position;

    private Long designation;

    private Boolean isPrimary;

}
