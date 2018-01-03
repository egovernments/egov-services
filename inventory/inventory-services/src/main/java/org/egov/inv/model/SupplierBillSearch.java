package org.egov.inv.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierBillSearch {

    private String tenantId;

    private List<String> ids;

    private String store;

    private String invoiceNumber;

    private Long invoiceDate;

    private Long approvedDate;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private Integer pageNumber;
}
