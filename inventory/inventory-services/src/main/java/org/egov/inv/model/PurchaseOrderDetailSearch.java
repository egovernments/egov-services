package org.egov.inv.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDetailSearch {

    private List<String> ids;

    private String tenantId;

    private String purchaseOrder;

    private String material;

    private String ordernumber;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
}
