package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FunctionContract;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_billdetail";
	public static final String SEQUENCE_NAME = "seq_egf_billdetail";
    private String id;
    private String billRegisterId;
    private Integer orderId;
    private String chartOfAccountId;
    private String glcode;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String functionId;

    public BillDetail toDomain() {
	BillDetail billDetail = new BillDetail();
	super.toDomain(billDetail);
	billDetail.setId(this.id);
	billDetail.setOrderId(this.orderId);
	billDetail.setChartOfAccount(ChartOfAccountContract.builder().id(chartOfAccountId).build());
	billDetail.setGlcode(this.glcode);
	billDetail.setDebitAmount(this.debitAmount);
	billDetail.setCreditAmount(this.creditAmount);
	billDetail.setFunction(FunctionContract.builder().id(functionId).build());
	return billDetail;
    }

    public BillDetailEntity toEntity(BillDetail billDetail) {
	super.toEntity((Auditable) billDetail);
	this.id = billDetail.getId();
	this.orderId = billDetail.getOrderId();
	this.chartOfAccountId = billDetail.getChartOfAccount() != null ? billDetail.getChartOfAccount().getId() : null;
	this.glcode = billDetail.getGlcode();
	this.debitAmount = billDetail.getDebitAmount();
	this.creditAmount = billDetail.getCreditAmount();
	this.functionId = billDetail.getFunction() != null ? billDetail.getFunction().getId() : null;
	return this;
    }
}
