package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1>UserAuthResponseInfo</h1>
 * 
 * @author Narendra
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAuthResponseInfo {
	private String access_token;
	private String token_type;
	private String refresh_token;
	private String expires_in;
	private String scope;
	private ResponseInfo responseInfo;
	private UserRequest userRequest;
}
