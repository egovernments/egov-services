package org.egov.pgr.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Department {
	private Long id;
	private String name;
	private String code;

}
