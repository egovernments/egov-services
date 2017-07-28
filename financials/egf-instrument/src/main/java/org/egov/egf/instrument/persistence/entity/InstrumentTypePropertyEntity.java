package org.egov.egf.instrument.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.InstrumentTypeProperty;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.master.web.contract.FinancialStatusContract;

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
public class InstrumentTypePropertyEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_instrumenttypeproperty";
	private String id;
	private String transactionTypeId;
	private Boolean reconciledOncreate;
	private String statusOnCreateId;
	private String statusOnUpdateId;
	private String statusOnReconcileId;

	public InstrumentTypeProperty toDomain() {
		InstrumentTypeProperty instrumentTypeProperty = new InstrumentTypeProperty();
		super.toDomain(instrumentTypeProperty);
		instrumentTypeProperty.setTransactionType(TransactionType.valueOf(this.transactionTypeId));
		instrumentTypeProperty.setReconciledOncreate(this.reconciledOncreate);
		instrumentTypeProperty.setStatusOnCreate(FinancialStatusContract.builder().code(statusOnCreateId).build());
		instrumentTypeProperty.setStatusOnUpdate(FinancialStatusContract.builder().code(statusOnUpdateId).build());
		instrumentTypeProperty.setStatusOnReconcile(FinancialStatusContract.builder().code(statusOnReconcileId).build());
		return instrumentTypeProperty;
	}

	public InstrumentTypePropertyEntity toEntity(InstrumentTypeProperty instrumentTypeProperty) {
		super.toEntity(instrumentTypeProperty);
		
		 this.transactionTypeId = instrumentTypeProperty.getTransactionType()
		 != null ? instrumentTypeProperty.getTransactionType().toString() : null;
	
		this.reconciledOncreate = instrumentTypeProperty.getReconciledOncreate();
		this.statusOnCreateId = instrumentTypeProperty.getStatusOnCreate() != null
				? instrumentTypeProperty.getStatusOnCreate().getCode() : null;
		this.statusOnUpdateId = instrumentTypeProperty.getStatusOnUpdate() != null
				? instrumentTypeProperty.getStatusOnUpdate().getCode() : null;
		this.statusOnReconcileId = instrumentTypeProperty.getStatusOnReconcile() != null
				? instrumentTypeProperty.getStatusOnReconcile().getCode() : null;
		return this;
	}

}
