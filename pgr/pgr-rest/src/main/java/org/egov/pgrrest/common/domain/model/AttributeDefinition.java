package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class AttributeDefinition {
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    private boolean readOnly;
    private AttributeDataType dataType;
    private boolean required;
    private String dataTypeDescription;
    private int order;
    private String description;
    private String code;
    private String url;
    private String groupCode;
    private List<AttributeRolesDefinition> roles;
    private List<AttributeActionsDefinition> actions;
    private List<ComputeRuleDefinition> computeRules;
    private List<ValueDefinition> values;

    public List<String> getRoleNames(){
        return roles.stream()
            .map(AttributeRolesDefinition :: getName)
            .collect(Collectors.toList());
    }

    public List<String> getActionNames(){
        return actions.stream()
            .map(action -> action.getAction().getCode())
            .collect(Collectors.toList());
    }

    public boolean isMultiValueType() {
        return dataType == AttributeDataType.MULTI_VALUE_LIST;
    }

    public Object parse(AttributeEntry attributeEntry) {
        final String stringValue = attributeEntry.getCode();

        if (dataType == AttributeDataType.DOUBLE) {
            return Double.parseDouble(stringValue);
        } else if (dataType == AttributeDataType.DATE) {
            return LocalDate.parse(attributeEntry.getCode(), DateTimeFormat.forPattern(DATE_FORMAT));
        } else if (dataType == AttributeDataType.INTEGER) {
            return Integer.parseInt(stringValue);
        } else if (dataType == AttributeDataType.DATE_TIME) {
            return LocalDateTime.parse(attributeEntry.getCode(), DateTimeFormat.forPattern(DATE_TIME_FORMAT));
        } else if (dataType == AttributeDataType.LONG) {
            return Long.parseLong(stringValue);
        }
        /* else if (dataType == AttributeDataType.TEXTAREA)
         {
        	 return stringValue;
         }*/
        return stringValue;
    }

    public Object parse(List<AttributeEntry> attributeEntries) {
        if (dataType == AttributeDataType.MULTI_VALUE_LIST) {
            return attributeEntries.stream()
                .map(AttributeEntry::getCode)
                .collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }
}

