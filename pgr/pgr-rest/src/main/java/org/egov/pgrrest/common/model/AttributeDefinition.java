package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
    private String url;
    private String groupCode;
    private List<AttributeRolesDefinition> roles;
    private List<AttributeActionsDefinition> actions;
    private List<ValueDefinition> values;

    public List<String> getRoleNames(){
        return roles.stream()
            .map(AttributeRolesDefinition :: getName)
            .collect(Collectors.toList());
    }

    public List<String> getActionNames(){
        return actions.stream()
            .map(AttributeActionsDefinition :: getName)
            .collect(Collectors.toList());
    }
}
