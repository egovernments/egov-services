package org.egov.pgr.common.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Position")
    private List<Position> position;

    public org.egov.pgr.common.model.Position toDomain() {
        if (CollectionUtils.isEmpty(position)) {
            return null;
        }
        return new org.egov.pgr.common.model.Position(position.get(0).getName());
    }
}