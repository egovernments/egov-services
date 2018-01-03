package org.egov.works.services.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrder {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("workOrderDate")
    private Long workOrderDate = null;

    @JsonProperty("workOrderNumber")
    private String workOrderNumber = null;

}
