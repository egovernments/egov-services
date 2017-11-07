package org.egov.swm.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.swm.domain.model.*;

import java.util.List;

@Getter
public class BoundaryResponse {

    @JsonProperty("Boundary")
    private List<org.egov.swm.domain.model.Boundary> boundaries;

}
