package org.egov.inv.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Store {
    
    private String id; 
    
    @NotNull
    private String code;
    
    @NotNull
    private String name;
    
    @NotNull
   
    private String description; 
    
    @NotNull
    private Department department;
    
    @NotNull
    private Employee storeInCharge;
    
    @NotNull
    private String billingAddress;
    
    @NotNull
    private String deliveryAddress;
    
    @NotNull
    private String contactNo1;
    
    private String contactNo2;
    
    @NotNull
    private String email;
    
    private Boolean isCentralStore;
    
    private Boolean active;
    
    private AuditDetails auditDetails;

}
