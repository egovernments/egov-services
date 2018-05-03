package org.egov.user.contract;

import org.egov.common.contract.request.AuditDetails;
import org.egov.common.contract.request.UserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User extends UserInfo  {

  @JsonProperty("active")
  private Boolean active;

  @JsonProperty("accountLocked")
  private Boolean accountLocked;

  @JsonProperty("userDetails")
  private UserDetails userDetails;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails;

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + this.getId().hashCode();
	result = prime * result + this.getTenantId().hashCode();
	return result;
}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;

		if (this.getId().equals(other.getId()) && this.getTenantId().equals(other.getTenantId())) {
			return true;
		} else
			return false;
	}

}
