package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AttributeDefinition {
    private boolean readOnly;
    private String dataType;
    private boolean required;
    private String dataTypeDescription;
    private int order;
    private String description;
    private String code;
    private List<ValueDefinition> values;
}
