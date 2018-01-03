package org.egov.inv.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierBillAdvanceAdjustmentSearch {

    private List<String> ids;

    private String tenantId;

    private String supplierBill;

    private String supplierAdvanceRequisition;

    private BigDecimal advanceAdjustedAmount;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private Integer pageNumber;
}
