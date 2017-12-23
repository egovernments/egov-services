package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;

import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.web.contract.AccountDetailKey;
import org.egov.egf.bill.web.contract.AccountDetailType;

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
public class BillPayeeDetailEntity {
    public static final String TABLE_NAME = "egf_billpayeedetail";
    public static final String SEQUENCE_NAME = "seq_egf_billpayeedetail";
    private String tenantId;
    private String id;
    private String billDetail;
    private String accountDetailType;
    private String accountDetailKey;
    private BigDecimal amount;
    private String createdBy;
    private String lastModifiedBy;
    private Long createdTime;
    private Long lastModifiedTime;

    public BillPayeeDetail toDomain() {
        final BillPayeeDetail billPayeeDetail = new BillPayeeDetail();
        billPayeeDetail.setTenantId(tenantId);
        billPayeeDetail.setId(id);
        billPayeeDetail.setAccountDetailType(AccountDetailType.builder().id(accountDetailType).build());
        billPayeeDetail.setAccountDetailKey(AccountDetailKey.builder().id(accountDetailKey).build());
        billPayeeDetail.setAmount(amount);
        billPayeeDetail.setAuditDetails(new AuditDetails());
        billPayeeDetail.getAuditDetails().setCreatedBy(createdBy);
        billPayeeDetail.getAuditDetails().setCreatedTime(createdTime);
        billPayeeDetail.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        billPayeeDetail.getAuditDetails().setLastModifiedTime(lastModifiedTime);
        return billPayeeDetail;
    }

    public BillPayeeDetailEntity toEntity(final BillPayeeDetail billPayeeDetail) {
        tenantId = billPayeeDetail.getTenantId();
        id = billPayeeDetail.getId();
        accountDetailType = billPayeeDetail.getAccountDetailType() != null ? billPayeeDetail.getAccountDetailType().getId()
                : null;
        accountDetailKey = billPayeeDetail.getAccountDetailKey() != null ? billPayeeDetail.getAccountDetailKey().getId() : null;
        amount = billPayeeDetail.getAmount();
        createdBy = billPayeeDetail.getAuditDetails() != null ? billPayeeDetail.getAuditDetails().getCreatedBy() : null;
        createdTime = billPayeeDetail.getAuditDetails() != null ? billPayeeDetail.getAuditDetails().getCreatedTime() : null;
        lastModifiedBy = billPayeeDetail.getAuditDetails() != null ? billPayeeDetail.getAuditDetails().getLastModifiedBy() : null;
        lastModifiedTime = billPayeeDetail.getAuditDetails() != null ? billPayeeDetail.getAuditDetails().getLastModifiedTime()
                : null;
        return this;
    }
}
