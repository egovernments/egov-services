package org.egov.propertyUser.model;

import org.egov.models.RequestInfo;
import org.egov.models.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>UserRequestInfo</h1> This model have User, List of User and RequestInfo
 * models This model class is used to send User request
 * 
 * @author S Anilkumar
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestInfo {
	@JsonProperty("User")
	private User user;

	// @JsonProperty("user")
	// private List<User> user;

	@JsonProperty("RequestInfo")
	private RequestInfo RequestInfo;
}
