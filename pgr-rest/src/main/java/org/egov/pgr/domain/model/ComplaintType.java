package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class ComplaintType {
    private String name;
    private String code;
}
