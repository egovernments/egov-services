package org.egov.egf.master.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SubSchemeSearch extends SubScheme {
	private Integer pageSize;
	private Integer offSet;
}