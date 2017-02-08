package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class AssigneeResponse {

    private Long id;
    private String name;

}
