package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Role {
    
    private Long id;
    private String name;
}
