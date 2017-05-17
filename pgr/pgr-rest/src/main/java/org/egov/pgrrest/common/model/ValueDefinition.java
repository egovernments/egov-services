package org.egov.pgrrest.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValueDefinition {
    private String name;
    private String key;
    private boolean active;
}
