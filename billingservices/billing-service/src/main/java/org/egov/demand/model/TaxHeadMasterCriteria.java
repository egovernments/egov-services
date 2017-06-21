package org.egov.demand.model;

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
public class TaxHeadMasterCriteria {

	@NotNull
	private String tenantId;
	@NotNull
	private String service;
	private String category;
	private String name;
	private String code;
	private String glCode;
	private Boolean isDebit;
	private Boolean isActualDemand;
	
	
	private Long size;
	private Long offset;
}
