package org.egov.user.model;

import java.util.List;
import java.util.Set;

import org.egov.user.model.enums.Type;

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
public class UserSearchCriteria {
	
	private String tenantId;
	private Long lastChangedSince;
	private Set<String> userName;
	private Boolean active;
	private Set<Long> id;
	private Type type;
	private Set<String> roleCodes;
	private Boolean includeDetails;
	private Integer pageSize;
	private Integer pageNumber;
	private Set<String> sort;	
	

}
