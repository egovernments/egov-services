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
public class PriceListDetailsSearchRequest {

    private String tenantId;

    private String ids;

    private String id;
    
    private String priceList;

    private String material;
    
    private Long fromDate;
    
    private Long toDate;

    private Double ratePerUnit;
    
    private Double quantity;
    
    private Uom uom;

    private Boolean active;
    
    private Boolean deleted;
    
    private Integer pageSize;

    private Integer offSet;

    private String sortBy;

}
