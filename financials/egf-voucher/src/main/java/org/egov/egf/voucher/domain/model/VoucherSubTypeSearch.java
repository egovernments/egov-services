package org.egov.egf.voucher.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSubTypeSearch extends VoucherSubType {

    private String ids;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;
}
