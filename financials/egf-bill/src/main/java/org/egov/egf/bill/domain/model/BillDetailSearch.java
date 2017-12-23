package org.egov.egf.bill.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailSearch extends BillDetail {
    private String ids;
    private String billNumber;
    private String billNumbers;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}
