package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assignment {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("department")
    private Long department;

    @JsonProperty("designation")
    private Long designation;

}
