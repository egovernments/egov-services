package org.egov.egf.master.persistence.entity;

import java.util.Date;

import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;

import lombok.Data;

@Data
public class FundSearchEntity extends FundEntity  {
	private Date fromDate;
	private Date toDate;
	private Integer pageSize=10;
	private Integer offset =0;
	public  FundSearchEntity toEntity(FundSearch fundSearch) {
		
		super.toEntity((Fund)fundSearch);
		this.pageSize=fundSearch.getPageSize();
	     return this;
		
	}


}
