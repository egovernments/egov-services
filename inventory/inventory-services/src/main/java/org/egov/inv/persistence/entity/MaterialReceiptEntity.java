package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.Store;
import org.egov.inv.model.Supplier;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialReceiptEntity {
    public static final String TABLE_NAME = "materialreceipt";
    public static final String SEQUENCE_NAME = "seq_materialreceipt";
    public static final String ALIAS = "materialreceipt";


    private String id;

    private String mrnNumber;

    private String issueNumber;

    private Long receiptDate;

    private String receiptType;

    private String financialYear;

    private String receiptPurpose;

    private String receivingStore;

    private String issueingStore;

    private String supplierCode;

    private String supplierBillNo;

    private Long supplierBillDate;

    private String challanNo;

    private Long challanDate;

    private String description;

    private String receivedBy;

    private String designation;

    private String bill;

    private String inspectedBy;

    private Long inspectionDate;

    private String mrnStatus;

    private String inspectionRemarks;

    private String receiptdetailsId;

    private BigDecimal totalReceiptValue;

    private String fileStoreId;

    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

    private String tenantId;

    private String paymentTerms;

    public MaterialReceipt toDomain() {
        MaterialReceipt materialReceipt = new MaterialReceipt();

        return materialReceipt.id(id)
                .mrnNumber(mrnNumber)
                .issueNumber(issueNumber)
                .receiptDate(receiptDate)
                .receiptType(MaterialReceipt.ReceiptTypeEnum.fromValue(receiptType))
                .financialYear(financialYear)
                .receiptPurpose(null != receiptPurpose ? MaterialReceipt.ReceiptPurposeEnum.fromValue(receiptPurpose) : null)
                .receivingStore(!isEmpty(receivingStore) ? buildStore(receivingStore) : null)
                .issueingStore(!isEmpty(issueingStore) ? buildStore(issueingStore) : null)
                .supplier(!isEmpty(supplierCode) ? buildSupplier() : null)
                .supplierBillNo(supplierBillNo)
                .supplierBillDate(supplierBillDate)
                .challanNo(challanNo)
                .challanDate(challanDate)
                .description(description)
                .receivedBy(receivedBy)
                .designation(designation)
                .bill(bill)
                .inspectedBy(inspectedBy)
                .inspectionDate(inspectionDate)
                .mrnStatus(null != mrnStatus ? MaterialReceipt.MrnStatusEnum.valueOf(mrnStatus) : null)
                .inspectionRemarks(inspectionRemarks)
                .receiptDetails(Collections.emptyList())
                .totalReceiptValue(totalReceiptValue)
                .fileStoreId(fileStoreId)
                .paymentTerms(paymentTerms)
                .auditDetails(buildAuditDetails());
    }


    private Supplier buildSupplier() {
        Supplier supplier = new Supplier();
        return supplier.code(supplierCode);
    }

    private Store buildStore(String storeCode) {
        Store store = new Store();
        return store.code(storeCode);
    }

    private AuditDetails buildAuditDetails() {
        AuditDetails auditDetails = new AuditDetails();
        return auditDetails
                .createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastModifiedBy)
                .lastModifiedTime(lastModifiedTime);
    }
}
