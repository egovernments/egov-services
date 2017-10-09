package org.egov.propertyUser.model;

import org.egov.models.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1>UserSearchRequestInfo</h1> This model class is used to send User search
 * request
 * 
 * @author S Anilkumar
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequestInfo {

	private String tenantId;

	private String username;

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
