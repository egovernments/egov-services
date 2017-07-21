package org.egov.egf.instrument.persistence.entity;

import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SurrenderReasonSearchEntity extends SurrenderReasonEntity {
	private Integer pageSize;
	private Integer offset;
	private String sortBy;
	private String ids;

	@Override
	public SurrenderReason toDomain() {
		SurrenderReason surrenderReason = new SurrenderReason();
		super.toDomain(surrenderReason);
		return surrenderReason;
	}

	public SurrenderReasonSearchEntity toEntity(SurrenderReasonSearch surrenderReasonSearch) {
		super.toEntity((SurrenderReason) surrenderReasonSearch);
		this.pageSize = surrenderReasonSearch.getPageSize();
		this.offset = surrenderReasonSearch.getOffset();
		return this;
	}

}