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

}

