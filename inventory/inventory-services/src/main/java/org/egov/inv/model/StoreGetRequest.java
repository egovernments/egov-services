package org.egov.inv.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreGetRequest {

    private List<String> id;

    private List<String> code;

    private String name;

    private String description;

    private String department;

    private String contactNo1;
    
    private String billingAddress;
    
    private String deliveryAddress;
    
    private String contactNo2;

    private String email;

    private Boolean isCentralStore;

    private String storeInCharge;

    private Boolean active;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    @NotNull
    private String tenantId;
}
