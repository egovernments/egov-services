package org.egov.inv.persistence.entity;

import lombok.*;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.Store;
import org.egov.inv.model.Supplier;

import java.math.BigDecimal;
import java.util.Collections;

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

    private String mrnnumber;

    private Long receiptdate;

    private String receipttype;

    private String financialyear;

    private String receiptpurpose;

    private String receivingstore;

    private String issueingstore;

    private String suppliercode;

    private String supplierbillno;

    private Long supplierbilldate;

    private String challanno;

    private Long challandate;

    private String description;

    private String receivedby;

    private String designation;

    private String bill;

    private String inspectedby;

    private Long inspectiondate;

    private String mrnstatus;

    private String inspectionremarks;

    private String receiptdetailsid;

    private BigDecimal totalreceiptvalue;

    private String filestoreid;

    private String createdby;

    private Long createdtime;

    private String lastmodifiedby;

    private Long lastmodifiedtime;

    private String tenantid;

    private String paymentterms;

    public MaterialReceipt toDomain() {
        MaterialReceipt materialReceipt = new MaterialReceipt();

        return materialReceipt.id(id)
                .mrnNumber(mrnnumber)
                .receiptDate(receiptdate)
                .receiptType(MaterialReceipt.ReceiptTypeEnum.fromValue(receipttype))
                .financialYear(financialyear)
                .receiptPurpose(null != receiptpurpose ? MaterialReceipt.ReceiptPurposeEnum.fromValue(receiptpurpose) : null)
                .receivingStore(buildStore(receivingstore))
                .issueingStore(buildStore(issueingstore))
                .supplier(buildSupplier())
                .supplierBillNo(supplierbillno)
                .supplierBillDate(supplierbilldate)
                .challanNo(challanno)
                .challanDate(challandate)
                .description(description)
                .receivedBy(receivedby)
                .designation(designation)
                .bill(bill)
                .inspectedBy(inspectedby)
                .inspectionDate(inspectiondate)
                .mrnStatus(null != mrnstatus ? MaterialReceipt.MrnStatusEnum.valueOf(mrnstatus) : null)
                .inspectionRemarks(inspectionremarks)
                .receiptDetails(Collections.emptyList())
                .totalReceiptValue(totalreceiptvalue)
                .fileStoreId(filestoreid)
                .paymentTerms(paymentterms)
                .auditDetails(buildAuditDetails());
    }


    private Supplier buildSupplier() {
        Supplier supplier = new Supplier();
        return supplier.code(suppliercode);
    }

    private Store buildStore(String storeCode) {
        Store store = new Store();
        return store.code(storeCode);
    }

    private AuditDetails buildAuditDetails() {
        AuditDetails auditDetails = new AuditDetails();
        return auditDetails
                .createdBy(createdby)
                .createdTime(createdtime)
                .lastModifiedBy(lastmodifiedby)
                .lastModifiedTime(lastmodifiedtime);
    }
}
