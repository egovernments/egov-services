package org.egov.inv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceListSearchRequest {

    private String tenantId;

    private String ids;

    private String id;

    private String supplierName;
    
    private String suppliers;
    
    private String rateType;

    private String rateContractNumber;
    
    private String rateContractNumbers;

    private String agreementNumber;
    
    private String agreementNumbers;

    private Long rateContractDate;
    
    private Long agreementDate;
    
    private Long agreementStartDate;
    
    private Long agreementEndDate;
    
    private Boolean active;

    private Integer pageSize;

    private Integer offSet;

    private String sortBy;
    
    private String materialCode;

}
