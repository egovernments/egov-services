package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssigneeRequestInfo {

    @JsonProperty("ResposneInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("AssigneeFilterInfo")
    private AssigneeFilterInfo assigneeFilterInfo = null;

}
