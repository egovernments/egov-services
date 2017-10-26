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
    @Size(min=5,max=50)
//    @Pattern(regexp="^[A-Za-z0-9]*$")
    private String code;
    
    @NotNull
    @Size(min=5,max=50)
    private String name;
    
    @NotNull
    @Size(max=1000)
 //   @Pattern(regexp="^[A-Za-z0-9]*$")
    private String description; 
    
    @NotNull
    private Department department;
    
    @NotNull
    private Employee storeInCharge;
    
    @NotNull
    @Size(max=1000)
 //   @Pattern(regexp="^[A-Za-z0-9]*$")
    private String billingAddress;
    
    @NotNull
    @Size(max=1000)
 //   @Pattern(regexp="^[A-Za-z0-9]*$")
    private String deliveryAddress;
    
    @NotNull
    @Size(max=10)
    private String contactNo1;
    
    private String contactNo2;
    
    @NotNull
    @Size(max=100)
    private String email;
    
    private Boolean isCentralStore;
    
    private Boolean active;
    
    private AuditDetails auditDetails;

}
