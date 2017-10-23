package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class DemolitionReasonSearchCriteria {

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	private Long id;

	private String name;

	private String code;

	private String description;

	private Integer pageSize;

	private Integer offSet;
}
