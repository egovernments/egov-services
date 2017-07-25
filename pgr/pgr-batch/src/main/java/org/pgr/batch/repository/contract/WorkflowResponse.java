package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
public class WorkflowResponse {

    public static final String STATE_ID = "systemStateId";

    @JsonProperty("assignee")
    private String assignee;

    @JsonProperty("values")
    private Map<String, Attribute> values;

    public String getValueForKey(String key) {
        if (Objects.nonNull(values.get(key)))
            return values.get(key).getValues().get(0).getName();

        return "";
    }

}
