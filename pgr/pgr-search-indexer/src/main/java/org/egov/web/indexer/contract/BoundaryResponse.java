package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class BoundaryResponse {

    @JsonProperty("Boundary")
    private List<Boundary> boundaries;

}
