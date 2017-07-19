package org.egov.egf.instrument.persistence.entity;

import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentTypeSearchEntity extends InstrumentTypeEntity {
	private Integer pageSize;
	private Integer offset;
	private String sortBy;
	private String ids;

	@Override
	public InstrumentType toDomain() {
		InstrumentType instrumentType = new InstrumentType();
		super.toDomain(instrumentType);
		return instrumentType;
	}

	public InstrumentTypeSearchEntity toEntity(InstrumentTypeSearch instrumentTypeSearch) {
		super.toEntity((InstrumentType) instrumentTypeSearch);
		this.pageSize = instrumentTypeSearch.getPageSize();
		this.offset = instrumentTypeSearch.getOffset();
		return this;
	}

}