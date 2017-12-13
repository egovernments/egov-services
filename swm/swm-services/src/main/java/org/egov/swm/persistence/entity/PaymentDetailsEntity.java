package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.PaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetails;

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
public class PaymentDetailsEntity {

    private String tenantId = null;

    private String code;

    private String paymentNo;

    private String voucherNumber;

    private Long voucherDate;

    private String instrumentType;

    private String instrumentNumber;

    private Long instrumentDate;

    private Double amount;

    private String bankName;

    private String branchName;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public PaymentDetails toDomain() {

        final PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTenantId(tenantId);
        paymentDetails.setCode(code);
        paymentDetails.setVendorPaymentDetails(VendorPaymentDetails.builder().paymentNo(paymentNo).build());
        paymentDetails.setVoucherNumber(voucherNumber);
        paymentDetails.setVoucherDate(voucherDate);
        paymentDetails.setInstrumentType(instrumentType);
        paymentDetails.setInstrumentNumber(instrumentNumber);
        paymentDetails.setInstrumentDate(instrumentDate);
        paymentDetails.setAmount(amount);
        paymentDetails.setBankName(bankName);
        paymentDetails.setBranchName(branchName);
        paymentDetails.setAuditDetails(new AuditDetails());
        paymentDetails.getAuditDetails().setCreatedBy(createdBy);
        paymentDetails.getAuditDetails().setCreatedTime(createdTime);
        paymentDetails.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        paymentDetails.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return paymentDetails;

    }

}
