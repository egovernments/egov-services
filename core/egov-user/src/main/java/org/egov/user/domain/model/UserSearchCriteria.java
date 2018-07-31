package org.egov.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.user.domain.exception.InvalidUserSearchCriteriaException;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Getter
@Builder
public class UserSearchCriteria {

    private List<Long> id;
    private List<String> uuid;
    private String userName;
    private String name;
    private String mobileNumber;
    private String aadhaarNumber;
    private String pan;
    private String emailId;
    private boolean fuzzyLogic;
    private Boolean active;
    private int pageSize;
    private int pageNumber;
    private List<String> sort;
    private String type;
    private String tenantId;
    private List<String> roleCodes;

    public void validate() {
    	boolean isInvalid = isTenantIdAbsent();
    	if (isInvalid) {
    		throw new InvalidUserSearchCriteriaException(this);
		}
	}

	public boolean isTenantIdAbsent() {
    	return isEmpty(tenantId);
	}
}
