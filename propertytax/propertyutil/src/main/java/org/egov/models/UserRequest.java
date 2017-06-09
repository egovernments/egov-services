package org.egov.models;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h1>UserRequest</h1>
 * @author S Anilkumar
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

	private String id;
	private String userName;
	private String name;
	private String mobileNumber;
	private String emailId;
	private String locale;
	private String type;
	private List<Role> roles;
}
