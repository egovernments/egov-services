package org.egov.lcms.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * @author Shubham Pratap
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvocatePaymentSearchCriteria {
	
	@NotNull
	@JsonProperty("tenantId")
	private String tenantId;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String sort;
	
	private String[] code;
	
	private String advocateName;
	
	private Long fromDate;
	
	private Long toDate;
}
