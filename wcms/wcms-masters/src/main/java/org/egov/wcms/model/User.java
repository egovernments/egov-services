package org.egov.wcms.model;

import java.util.List;

import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class User {
	
	@NotNull
	private long id;
	
	@NotNull
	private String userName;
	
	@NotNull
	private String authToken;
	
	@NotNull
	private String salutation;
	
	@NotNull
	private String name;
	
	@NotNull
	private String gender;
	
	@NotNull
	private String mobileNumber;
	
	@NotNull
	private String emailId;
	
	@NotNull
	private String aadhaarNumber;
	
	@NotNull
	private boolean active;
	
	@NotNull
	private long pwdExpiryDate;
	
	@NotNull
	private String locale;
	
	@NotNull
	private String type;
	
	@NotNull
	private boolean accountLocked;
	
	@NotNull
	private List<Role> roles;
	
	@NotNull
	private UserDetails userDetails;
	
	@NotNull
	private AuditDetails auditDetails; 
	
	

}
