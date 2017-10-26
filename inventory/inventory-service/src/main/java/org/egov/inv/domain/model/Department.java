package org.egov.inv.domain.model;

import lombok.Data;

@Data
public class Department {

    private String id;
    
    private String code;
    
    private String name;
    
    private Boolean active;
}
