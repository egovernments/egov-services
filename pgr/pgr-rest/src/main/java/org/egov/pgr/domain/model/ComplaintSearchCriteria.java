package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ComplaintSearchCriteria {
	private String serviceRequestId;
	private String serviceCode;
	private Date startDate;
	private Date endDate;
	private String status;
	private Date lastModifiedDatetime;
	private Long userId;
	private Long assignmentId;
	private String name;
	private String mobileNumber;
	private String emailId;
	private Long receivingMode;
	private Long locationId;
	private Long childLocationId;


}