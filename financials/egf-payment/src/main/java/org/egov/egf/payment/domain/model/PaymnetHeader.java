package org.egov.egf.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * Created by ritesh on 25/9/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymnetHeader extends AuditableContract {

    @Length(max = 256)
    private String id;

    private String selected;

    private String type;

    private String concurrentDate;

    private BankAccountContract bankAccount;

    private BigDecimal paymentAmount;

    private VoucherContract voucher;



}