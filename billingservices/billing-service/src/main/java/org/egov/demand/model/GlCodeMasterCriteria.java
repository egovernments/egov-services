package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GlCodeMasterCriteria {

	@NotNull
	private String tenantId;
	@NotNull
	private String service;
	@NotNull
	private List<String> taxHead = new ArrayList<>();
	
	private List<String> id = new ArrayList<>();
	
	private Long fromDate;
	
	private Long toDate;
	
}
