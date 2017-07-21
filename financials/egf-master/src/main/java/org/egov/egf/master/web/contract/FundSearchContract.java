package org.egov.egf.master.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundSearchContract extends FundContract {

	private String sortBy;

	private Integer pageSize;

	private Integer offset;

	private List<Long> ids = new ArrayList<Long>();
	
	private Integer fromIndex;

	public boolean isPaginationCriteriaPresent() {
		return fromIndex != null && pageSize != null;
	}
}
