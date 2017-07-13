package org.egov.egf.master.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChartOfAccountSearch extends ChartOfAccount {
	private Integer pageSize;
	private Integer offset;
	private String sortBy;
}