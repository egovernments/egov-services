package org.egov.model.criteria;


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
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposalCriteria {
	@NotNull
	private String tenantId;
	private List<Long> id;
	private List<Long> assetId;
	private Long size;
	private Long offset;
	private Long fromDate;
	private Long toDate;
}
