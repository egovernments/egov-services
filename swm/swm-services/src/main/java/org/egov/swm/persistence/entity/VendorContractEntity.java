package org.egov.swm.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.PaymentTerms;
import org.egov.swm.domain.model.ServicesOffered;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorContract;

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
public class VendorContractEntity {

    private String tenantId = null;

    private String vendor = null;

    private String contractNo = null;

    private Long contractDate = null;

    private Long contractPeriodFrom = null;

    private Long contractPeriodTo = null;

    private Double securityDeposit = null;

    private Double paymentAmount = null;

    private String paymentTerms = null;

    private String remarks = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;
    
    private List<ServicesOffered> serviceOffered = new ArrayList<>();
    
    public VendorContract toDomain() {

        final VendorContract vendorContract = new VendorContract();
        vendorContract.setTenantId(tenantId);
        vendorContract.setVendor(Vendor.builder().vendorNo(vendor).build());
        vendorContract.setContractNo(contractNo);
        vendorContract.setContractDate(contractDate);
        vendorContract.setContractPeriodFrom(contractPeriodFrom);
        vendorContract.setContractPeriodTo(contractPeriodTo);
        vendorContract.setSecurityDeposit(securityDeposit);
        vendorContract.setPaymentAmount(paymentAmount);
        vendorContract.setPaymentTerms(PaymentTerms.builder().label(paymentTerms).build());
        vendorContract.setRemarks(remarks);
        vendorContract.setAuditDetails(new AuditDetails());
        vendorContract.getAuditDetails().setCreatedBy(createdBy);
        vendorContract.getAuditDetails().setCreatedTime(createdTime);
        vendorContract.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vendorContract.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return vendorContract;

    }

}
