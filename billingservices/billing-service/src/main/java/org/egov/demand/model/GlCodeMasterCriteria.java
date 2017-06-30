package org.egov.demand.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<String> taxHead = new HashSet<String>();
	private Set<String> id = new HashSet<String>();
	private String glCode;
	private Long fromDate;
	private Long toDate;
	private Long size;
	private Long offset;
	
}
