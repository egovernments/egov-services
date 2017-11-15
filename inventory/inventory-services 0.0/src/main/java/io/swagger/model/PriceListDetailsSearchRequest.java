package io.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PriceListDetailsSearchRequest {

    private String tenantId;

    private String ids;

    private String id;

    private String material;
    
    private Long fromDate;
    
    private Long toDate;

    private Double ratePerUnit;
    
    private Double quantity;
    
    private Uom uom;

    private Boolean active;
    
    private Integer pageSize;

    private Integer offSet;

    private String sortBy;

}
