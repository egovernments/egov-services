package org.egov.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@AllArgsConstructor
public class Otp {
	private static final String DEFAULT_TYPE = "REGISTER";
	private String mobileNumber;
    private String tenantId;
    private String type;

    @JsonIgnore
    public String getTypeOrDefault() {
    	return isEmpty(type) ? DEFAULT_TYPE : type;
	}
}
