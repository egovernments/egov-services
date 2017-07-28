package org.egov.egf.instrument.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.master.web.contract.ChartOfAccountContract;

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
public class InstrumentAccountCodeEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_instrumentaccountcode";
	private String id;
	private String instrumentTypeId;
	private String accountCodeId;

	public InstrumentAccountCode toDomain() {
		InstrumentAccountCode instrumentAccountCode = new InstrumentAccountCode();
		super.toDomain(instrumentAccountCode);
		instrumentAccountCode.setId(this.id);
		instrumentAccountCode.setInstrumentType(InstrumentType.builder().id(instrumentTypeId).build());
		instrumentAccountCode.setAccountCode(ChartOfAccountContract.builder().id(accountCodeId).build());
		return instrumentAccountCode;
	}

	public InstrumentAccountCodeEntity toEntity(InstrumentAccountCode instrumentAccountCode) {
		super.toEntity(instrumentAccountCode);
		this.id = instrumentAccountCode.getId();
		this.instrumentTypeId = instrumentAccountCode.getInstrumentType() != null
				? instrumentAccountCode.getInstrumentType().getId() : null;
		this.accountCodeId = instrumentAccountCode.getAccountCode() != null
				? instrumentAccountCode.getAccountCode().getId() : null;
		return this;
	}

}
