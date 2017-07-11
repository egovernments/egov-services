package org.egov.egf.master.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountDetailKeySearch extends AccountDetailKey {
	private Integer pageSize;
	private Integer offSet;
}