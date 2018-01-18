package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.web.contract.Employee;

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
public class VendorPaymentDetailsEntity {

    private String tenantId = null;

    private String paymentNo;

    private String vendorContract;

    private Double vendorInvoiceAmount;

    private String invoiceNo;

    private Long invoiceDate;

    private Long fromDate;

    private Long toDate;

    private String employee;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public VendorPaymentDetails toDomain() {

        final VendorPaymentDetails vendorPaymentDetails = new VendorPaymentDetails();
        vendorPaymentDetails.setTenantId(tenantId);
        vendorPaymentDetails.setPaymentNo(paymentNo);
        vendorPaymentDetails.setVendorContract(VendorContract.builder().contractNo(vendorContract).build());
        vendorPaymentDetails.setVendorInvoiceAmount(vendorInvoiceAmount);
        vendorPaymentDetails.setInvoiceNo(invoiceNo);
        vendorPaymentDetails.setInvoiceDate(invoiceDate);
        vendorPaymentDetails.setFromDate(fromDate);
        vendorPaymentDetails.setToDate(toDate);
        vendorPaymentDetails.setEmployee(Employee.builder().code(employee).build());
        vendorPaymentDetails.setAuditDetails(new AuditDetails());
        vendorPaymentDetails.getAuditDetails().setCreatedBy(createdBy);
        vendorPaymentDetails.getAuditDetails().setCreatedTime(createdTime);
        vendorPaymentDetails.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vendorPaymentDetails.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return vendorPaymentDetails;

    }

}
