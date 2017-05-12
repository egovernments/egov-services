package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.common.model.ValueDefinition;

@Getter
@AllArgsConstructor
@Builder
public class AttributeValueDefinition {
    private String key;
    private String name;

    public AttributeValueDefinition(ValueDefinition valueDefinition) {
        this.key = valueDefinition.getKey();
        this.name = valueDefinition.getName();
    }
}
