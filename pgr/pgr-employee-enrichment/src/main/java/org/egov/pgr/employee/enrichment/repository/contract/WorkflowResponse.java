package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class WorkflowResponse {

    public static final String STATE_ID = "systemStateId";

    @JsonProperty("assignee")
    private String positionId;

    @JsonProperty("values")
    private Map<String, Attribute> values;

    public String getValueForKey(String key) {
        if (Objects.nonNull(values.get(key)))
            return values.get(key).getValues().get(0).getName();

        return "";
    }

}
