package org.egov.pgr.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.domain.model.ValueDefinition;

@Getter
@AllArgsConstructor
@Builder
public class AttributeValueDefinition {
    private String key;
    private String name;
    @JsonProperty("isActive")
    private boolean active;
    private boolean required;

    public AttributeValueDefinition(ValueDefinition valueDefinition) {
        this.key = valueDefinition.getKey();
        this.name = valueDefinition.getName();
        this.active = valueDefinition.isActive();
        this.required = valueDefinition.isRequired();
    }

    public ValueDefinition toDomain(){
        return ValueDefinition.builder()
                    .key(key)
                    .name(name)
                    .active(active)
                    .required(required)
                    .build();
    }
}
