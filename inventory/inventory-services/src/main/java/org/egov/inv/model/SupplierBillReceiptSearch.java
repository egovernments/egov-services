package org.egov.inv.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierBillReceiptSearch {

    private List<String> ids;

    private String tenantId;

    private String supplierBill;

    private String materialReceipt;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private Integer pageNumber;
}
