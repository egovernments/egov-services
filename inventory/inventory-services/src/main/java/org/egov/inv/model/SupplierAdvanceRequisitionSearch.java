package org.egov.inv.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    private String status;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    private Integer pageNumber;
}
