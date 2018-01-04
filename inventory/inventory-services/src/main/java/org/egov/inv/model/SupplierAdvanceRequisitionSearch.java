package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAdvanceRequisitionSearch {

    private List<String> ids;

    private String tenantId;

    private String supplier;

    private String purchaseOrder;

    private BigDecimal advanceAdjustedAmount;

    private Boolean advanceFullyAdjustedInBill;

    private String stateId;

    private String sarStatus;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private Integer pageNumber;
}
