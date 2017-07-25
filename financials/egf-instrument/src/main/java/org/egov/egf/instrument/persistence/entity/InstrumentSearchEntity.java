package org.egov.egf.instrument.persistence.entity;

import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentSearchEntity extends InstrumentEntity {
	private String ids;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;

	@Override
	public Instrument toDomain() {
		Instrument instrument = new Instrument();
		super.toDomain(instrument);
		return instrument;
	}

	public InstrumentSearchEntity toEntity(InstrumentSearch instrumentSearch) {
		super.toEntity(instrumentSearch);
		this.pageSize = instrumentSearch.getPageSize();
		this.offset = instrumentSearch.getOffset();
		this.sortBy = instrumentSearch.getSortBy();
		this.ids = instrumentSearch.getIds();
		return this;
	}

}