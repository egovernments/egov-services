package org.egov.egf.voucher.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSubTypeSearchContract extends VoucherSubTypeContract {

    private String ids;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

}
