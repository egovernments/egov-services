package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.request.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner   {
	
	  private String tenantId;

	  private Long id;

	  private String userName;

	  private String authToken;

	  private String salutation;

	  private String name;

	  private String gender;

	  private String mobileNumber;

	  private String emailId;

	  private String aadhaarNumber;

	  private Boolean active;

	  private Long pwdExpiryDate;

	  private String locale;

	  private String type;

	  private Boolean accountLocked = false;

	  private List<Role> roles = new ArrayList<>();

	  private OwnerUserDetails userDetails;

	  private InstallmentAuditDetail auditDetails;
}
