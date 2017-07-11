package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FunctionarySearchEntity extends FunctionaryEntity {
	private Integer pageSize;
	private Integer offSet;

	@Override
	public Functionary toDomain() {
		Functionary functionary = new Functionary();
		super.toDomain(functionary);
		return functionary;
	}

	public FunctionarySearchEntity toEntity(FunctionarySearch functionarySearch) {
		super.toEntity((Functionary) functionarySearch);
		this.pageSize = functionarySearch.getPageSize();
		this.offSet = functionarySearch.getOffSet();
		return this;
	}

}