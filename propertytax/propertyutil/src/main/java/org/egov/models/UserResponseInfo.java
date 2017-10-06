package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1>UserResponseInfo</h1> This model have List of User and RequestInfo models
 * This model class is used to get User response
 * 
 * @author S Anilkumar
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseInfo {

	private List<User> user;

	private ResponseInfo responseInfo;

}
