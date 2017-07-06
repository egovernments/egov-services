package org.egov.propertyUser.model;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h1>UserSearchRequestInfo</h1>
 * This model class is used to send User search request 
 * 
 * @author S Anilkumar
 *
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequestInfo {

	private String tenantId;

	private String username;

	private RequestInfo requestInfo;

}
