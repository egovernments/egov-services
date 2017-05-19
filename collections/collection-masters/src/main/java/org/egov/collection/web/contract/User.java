package org.egov.collection.web.contract;

import lombok.*;

import java.util.List;

import org.egov.collection.domain.model.Role;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Long id;

	private String userName;

	private String name;

	private String mobileNumber;

	private String emailId;

	private String type;

	private String permanentAddress;

	private String tenantId;

	private List<Role> roles = null;
}
