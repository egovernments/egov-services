package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.common.model.Location;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class BoundaryResponse {
    private static final String BOUNDARY_NOT_FOUND_MESSAGE = "Boundary not found";
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
    @JsonProperty("Boundary")
    private List<Boundary> boundarys;

    public Location toDomain() {
        if (CollectionUtils.isEmpty(boundarys)) {
            throw new RuntimeException(BOUNDARY_NOT_FOUND_MESSAGE);
        }
        return new Location(boundarys.get(0).getName());
    }
}