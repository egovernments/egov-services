package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.FundsourceSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FundsourceSearchEntity extends FundsourceEntity {
	private Integer pageSize;
	private Integer offSet;

	@Override
	public Fundsource toDomain() {
		Fundsource fundsource = new Fundsource();
		super.toDomain(fundsource);
		return fundsource;
	}

	public FundsourceSearchEntity toEntity(FundsourceSearch fundsourceSearch) {
		super.toEntity((Fundsource) fundsourceSearch);
		this.pageSize = fundsourceSearch.getPageSize();
		this.offSet = fundsourceSearch.getOffSet();
		return this;
	}

}