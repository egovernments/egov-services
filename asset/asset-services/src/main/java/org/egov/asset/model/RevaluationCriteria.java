package org.egov.asset.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RevaluationCriteria {
	
	private String tenantId;
	private List<Long> id;
	private List<Long> assetId;
	private Long size;
	private Long offset;
	

}
