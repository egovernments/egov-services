package org.egov.swm.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class BoundaryResponse {

    @JsonProperty("Boundary")
    private List<org.egov.swm.domain.model.Boundary> boundaries;

}
