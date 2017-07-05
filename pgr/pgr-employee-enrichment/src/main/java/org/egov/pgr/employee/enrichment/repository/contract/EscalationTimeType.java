package org.egov.pgr.employee.enrichment.repository.contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EscalationTimeType {

    @JsonProperty("noOfHours")
    private long hours;
}
