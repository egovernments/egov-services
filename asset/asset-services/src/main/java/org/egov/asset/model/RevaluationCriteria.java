package org.egov.asset.model;

import java.util.List;

import org.egov.asset.model.enums.RevaluationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevaluationCriteria {
	
	private String tenantId;
	private List<Long> id;
	private List<Long> assetId;
	private RevaluationStatus status;
	private Long size;
	private Long offset;
	

}
