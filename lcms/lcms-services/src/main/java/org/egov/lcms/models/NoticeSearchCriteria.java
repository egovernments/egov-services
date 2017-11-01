package org.egov.lcms.models;

import javax.validation.constraints.NotNull;

import org.egov.lcms.enums.NoticeType;
import org.hibernate.validator.constraints.NotEmpty;

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
	

	@NotNull
	@NotEmpty
	private String tenantId;
	
	private String[] code;
	
	private String exhibitNo;
	
	private String applicant;
	
	private String defendant;
	
	private String advocateName;

    private NoticeType noticeType;
    
    private String[] sort;
    
    private Integer pageSize;

    private Integer pageNumber;
}
