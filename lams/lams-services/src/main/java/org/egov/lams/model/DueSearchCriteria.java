package org.egov.lams.model;

import java.util.Set;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DueSearchCriteria {
	private String tenantId;
	private Long assetCategory;
	private Long assetCode;
	private String agreementNumber;
	private Long electionWard;
	private Long locality;
	private Long zone;
	private Long revenueWard;
	private Long revenueBlock;
	private Long offset;
	private Long size;

}
