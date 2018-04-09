package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.UserSearchCriteria;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class UserSearchRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("id")
	private List<Long> id;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber;

	@JsonProperty("pan")
	private String pan;

	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("fuzzyLogic")
	private boolean fuzzyLogic;

	@JsonProperty("active")
	@Setter
	private Boolean active;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("pageSize")
	private int pageSize = 20;

	@JsonProperty("pageNumber")
	private int pageNumber = 0;

	@JsonProperty("sort")
	private List<String> sort = Collections.singletonList("name");

	@JsonProperty("userType")
	private String userType;

	@JsonProperty("roleCodes")
	private List<String> roleCodes;

	public UserSearchCriteria toDomain() {
		return UserSearchCriteria.builder()
				.id(id)
				.userName(userName)
				.name(name)
				.mobileNumber(mobileNumber)
				.aadhaarNumber(aadhaarNumber)
				.pan(pan)
				.emailId(emailId)
				.fuzzyLogic(fuzzyLogic)
				.active(active)
				.pageSize(pageSize)
				.pageNumber(pageNumber)
				.sort(sort)
				.type(userType)
				.tenantId(tenantId)
				.roleCodes(roleCodes)
				.build();
	}
}
