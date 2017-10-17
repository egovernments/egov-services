package org.egov.property.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeSearchCriteria {

	private String tenantId;

	private Integer pageSize;

	private Integer pageNumber;

	private String upicNumber;

	private String applicationNo;

	private String noticeType;

	private String noticeDate;

	private Long fromDate;

	private Long toDate;
}
