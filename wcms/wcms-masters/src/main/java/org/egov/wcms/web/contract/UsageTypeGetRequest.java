package org.egov.wcms.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UsageTypeGetRequest {
    private List<Long> ids;
    
    @NotNull
    private String tenantId;
    
    private String sortBy;
    
    private String sortOrder;
    
    private String parent;
    
    private String code;
    
    

}
