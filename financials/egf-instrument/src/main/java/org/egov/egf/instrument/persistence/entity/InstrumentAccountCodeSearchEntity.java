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
	private Integer pageSize;
	private Integer offset;
	private String sortBy;

	@Override
	public InstrumentAccountCode toDomain() {
		InstrumentAccountCode instrumentAccountCode = new InstrumentAccountCode();
		super.toDomain(instrumentAccountCode);
		return instrumentAccountCode;
	}

	public InstrumentAccountCodeSearchEntity toEntity(InstrumentAccountCodeSearch instrumentAccountCodeSearch) {
		super.toEntity((InstrumentAccountCode) instrumentAccountCodeSearch);
		this.pageSize = instrumentAccountCodeSearch.getPageSize();
		this.offset = instrumentAccountCodeSearch.getOffset();
		return this;
	}

}