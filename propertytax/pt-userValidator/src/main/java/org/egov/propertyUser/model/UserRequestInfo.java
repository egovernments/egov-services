package org.egov.propertyUser.model;

import java.util.List;

import org.egov.models.RequestInfo;
import org.egov.models.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>UserRequestInfo</h1>
 * This model have User, List of User and RequestInfo models
 * This model class is used to send User request 
 * 
 * @author S Anilkumar
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestInfo {
	@JsonProperty("User")
	private User User;

	@JsonProperty("user")
	private List<User> user;

	@JsonProperty("RequestInfo")
	private RequestInfo RequestInfo;
}
