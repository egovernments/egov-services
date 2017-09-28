package org.egov.egf.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.domain.model.User;
import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.voucher.web.contract.VoucherContract;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ritesh on 25/9/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MiscBillDeatils extends AuditableContract {


    private String id;

    private VoucherContract billVoucher;

    private VoucherContract payVoucher;

    private String billNumber;

    private Date billDate;

    private BigDecimal billAmount;

    private BigDecimal passedAmount;

    private BigDecimal paidAmount;

    private String paidTo;

    private User paidBy;
// Is it neccesory
//    private String amtInWords;

}
