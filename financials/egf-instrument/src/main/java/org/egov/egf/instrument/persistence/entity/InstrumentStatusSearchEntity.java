package org.egov.egf.instrument.persistence.entity;

import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentStatusSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentStatusSearchEntity extends InstrumentStatusEntity {
	private String ids;
	private Integer pageSize;
	private Integer offset;
	private String sortBy;

	@Override
	public InstrumentStatus toDomain() {
		InstrumentStatus instrumentStatus = new InstrumentStatus();
		super.toDomain(instrumentStatus);
		return instrumentStatus;
	}

	public InstrumentStatusSearchEntity toEntity(InstrumentStatusSearch instrumentStatusSearch) {
		super.toEntity((InstrumentStatus) instrumentStatusSearch);
		this.pageSize = instrumentStatusSearch.getPageSize();
		this.offset = instrumentStatusSearch.getOffset();
		return this;
	}

}