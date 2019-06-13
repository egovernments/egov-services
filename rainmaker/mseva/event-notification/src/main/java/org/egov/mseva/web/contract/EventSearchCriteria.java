package org.egov.mseva.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class EventSearchCriteria {
	
	private List<String> ids;
	
	private List<String> userids;
	
	private List<String> roles;
	
	private List<String> postedBy;
	
	private List<String> status;
	
	private String tenantId;
	
	private List<String> recepients;

	
}
