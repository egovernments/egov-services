package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class PositionResponse {

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