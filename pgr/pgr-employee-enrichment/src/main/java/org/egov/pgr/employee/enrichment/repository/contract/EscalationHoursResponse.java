package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class EscalationHoursResponse {
    @JsonProperty("EscalationTimeType")
    private List<EscalationTimeType> escalationTimeTypes;
}
