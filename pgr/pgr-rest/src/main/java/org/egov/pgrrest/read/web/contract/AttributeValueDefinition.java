package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.common.domain.model.ValueDefinition;

@Getter
@AllArgsConstructor
@Builder
public class AttributeValueDefinition {
    private String key;
    private String name;
    @JsonProperty("isActive")
    private boolean active;

    public AttributeValueDefinition(ValueDefinition valueDefinition) {
        this.key = valueDefinition.getKey();
        this.name = valueDefinition.getName();
        this.active = valueDefinition.isActive();
    }
}
