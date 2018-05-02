package org.egov.user.contract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchCriteria {
	
	@Builder.Default
	private Set<String> ids = new HashSet<>();
	
    private String userName;
    
    private String name;
    
    private String mobileNumber;
    
    private String aadhaarNumber;
    
    private String pan;
    
    private String emailId;
    
    private Boolean active;

    private int pageSize;
    
    private int pageNumber;
    
    private List<String> sort;
    
    private String type;
    
    private String tenantId;
    
    private List<String> roleCodes;
    
    private Boolean isActionsRequired;
    
    private String authToken;
    
    private Boolean isUserDetailRequired;
    
    private Boolean isUserStateLevel;
}
