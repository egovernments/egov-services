package org.egov.egf.instrument.persistence.entity;

import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentAccountCodeSearchEntity extends InstrumentAccountCodeEntity {
	private String ids;
	private String sortBy;
	private Integer pageSize;
	private Integer offset;

	@Override
	public InstrumentAccountCode toDomain() {
		InstrumentAccountCode instrumentAccountCode = new InstrumentAccountCode();
		super.toDomain(instrumentAccountCode);
		return instrumentAccountCode;
	}

	public InstrumentAccountCodeSearchEntity toEntity(InstrumentAccountCodeSearch instrumentAccountCodeSearch) {
		super.toEntity(instrumentAccountCodeSearch);
		this.pageSize = instrumentAccountCodeSearch.getPageSize();
		this.offset = instrumentAccountCodeSearch.getOffset();
		this.sortBy = instrumentAccountCodeSearch.getSortBy();
		this.ids = instrumentAccountCodeSearch.getIds();
		return this;
	}

}