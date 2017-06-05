package org.egov.asset.model;


import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DisposalCriteria {
	@NotNull
	private String tenantId;
	private List<Long> id;
	private List<Long> assetId;
	private Long size;
	private Long offset;
}
