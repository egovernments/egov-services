package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


@AllArgsConstructor
@Builder
@Value
public class Role {
    
    private Long id;
    private String name;
}
