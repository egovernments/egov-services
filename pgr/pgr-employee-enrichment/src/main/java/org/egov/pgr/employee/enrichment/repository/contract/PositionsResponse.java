package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PositionsResponse {

    @JsonProperty("Positions")
    List<PositionResponse> positions;

    public String getDesignationId() {
        return positions.get(0).getDepartmentDesignation().getDesignation().getId();
    }
}
