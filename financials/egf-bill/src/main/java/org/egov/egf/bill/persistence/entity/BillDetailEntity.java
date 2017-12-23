package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;

import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.web.contract.ChartOfAccount;
import org.egov.egf.bill.web.contract.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailEntity {
    public static final String TABLE_NAME = "egf_billdetail";
    public static final String SEQUENCE_NAME = "seq_egf_billdetail";
    private String tenantId;
    private String id;
    private String bill;
    private Integer orderId;
    private String chartOfAccount;
    private String glcode;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String function;
    private String createdBy;
    private String lastModifiedBy;
    private Long createdTime;
    private Long lastModifiedTime;

    public BillDetail toDomain() {
        final BillDetail billDetail = new BillDetail();
        billDetail.setTenantId(tenantId);
        billDetail.setId(id);
        billDetail.setOrderId(orderId);
        billDetail.setChartOfAccount(ChartOfAccount.builder().id(chartOfAccount).build());
        billDetail.setGlcode(glcode);
        billDetail.setDebitAmount(debitAmount);
        billDetail.setCreditAmount(creditAmount);
        billDetail.setFunction(Function.builder().id(function).build());
        billDetail.setAuditDetails(new AuditDetails());
        billDetail.getAuditDetails().setCreatedBy(createdBy);
        billDetail.getAuditDetails().setCreatedTime(createdTime);
        billDetail.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        billDetail.getAuditDetails().setLastModifiedTime(lastModifiedTime);
        return billDetail;
    }

    public BillDetailEntity toEntity(final BillDetail billDetail) {
        tenantId = billDetail.getTenantId();
        id = billDetail.getId();
        orderId = billDetail.getOrderId();
        chartOfAccount = billDetail.getChartOfAccount() != null ? billDetail.getChartOfAccount().getId() : null;
        glcode = billDetail.getGlcode();
        debitAmount = billDetail.getDebitAmount();
        creditAmount = billDetail.getCreditAmount();
        function = billDetail.getFunction() != null ? billDetail.getFunction().getId() : null;
        createdBy = billDetail.getAuditDetails() != null ? billDetail.getAuditDetails().getCreatedBy() : null;
        createdTime = billDetail.getAuditDetails() != null ? billDetail.getAuditDetails().getCreatedTime() : null;
        lastModifiedBy = billDetail.getAuditDetails() != null ? billDetail.getAuditDetails().getLastModifiedBy() : null;
        lastModifiedTime = billDetail.getAuditDetails() != null ? billDetail.getAuditDetails().getLastModifiedTime() : null;
        return this;
    }
}
