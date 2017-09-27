package org.egov.egf.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.master.web.contract.*;
import org.egov.egf.voucher.web.contract.VoucherContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ritesh on 27/9/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Remittance extends AuditableContract {

    private String id;

    private FinancialYearContract financialYear;

    private VoucherContract voucher;

    private RecoveryContract recovery;

    private FundContract fund;

    private BigDecimal month;

    List<RemittanceDetail> remittanceDetail = new ArrayList<>();

    private Date asOnDate;
}
