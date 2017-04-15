package org.egov.lams.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeCriteria {
	
	private Set<Long> id; 
	private String tenantId;
	private String noticeNo;
	private String agreementNumber;
	private String noticeNumber;
	private String ackNumber;
	private Long assetCategory;
	private Long offset;
	private Long size;

}
