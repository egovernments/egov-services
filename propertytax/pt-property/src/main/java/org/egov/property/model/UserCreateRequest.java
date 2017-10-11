package org.egov.property.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.models.User;

/**
 * This model has User and RequestInfo
 * models This model class is used to send User request
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
	@JsonProperty("User")
	private User user;

	@JsonProperty("RequestInfo")
	private org.egov.models.RequestInfo RequestInfo;
}
