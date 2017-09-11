package org.egov.lams.model;

import java.util.Set;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NoticeCriteria {
	
	private Set<Long> id; 
	private String tenantId;
	private String noticeNo;
	private String agreementNumber;
	private String ackNumber;
	private Long assetCategory;
	private Long offset;
	private Long size;

}
