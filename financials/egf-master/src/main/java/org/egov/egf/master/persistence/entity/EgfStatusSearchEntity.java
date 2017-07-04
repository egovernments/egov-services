package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.EgfStatus;
import org.egov.egf.master.domain.model.EgfStatusSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EgfStatusSearchEntity extends EgfStatusEntity {
	private Integer pageSize;
	private Integer offset;

	@Override
	public EgfStatus toDomain() {
		EgfStatus egfStatus = new EgfStatus();
		super.toDomain(egfStatus);
		return egfStatus;
	}

	public EgfStatusSearchEntity toEntity(EgfStatusSearch egfStatusSearch) {
		super.toEntity((EgfStatus) egfStatusSearch);
		this.pageSize = egfStatusSearch.getPageSize();
		this.offset = egfStatusSearch.getOffset();
		return this;
	}

}