package org.egov.egf.instrument.persistence.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentSearchEntity extends InstrumentEntity {
	private String ids;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;
	private String instrumentTypes;
	private String financialStatuses;
	private Date transactionFromDate;
	private Date transactionToDate;

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
		this.financialStatuses = instrumentSearch.getFinancialStatuses();
		this.instrumentTypes = instrumentSearch.getInstrumentTypes();
		this.transactionFromDate = instrumentSearch.getTransactionFromDate();
		this.transactionToDate = instrumentSearch.getTransactionToDate();
		return this;
	}

}