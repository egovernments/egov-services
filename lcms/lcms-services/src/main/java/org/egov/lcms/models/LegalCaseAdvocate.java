package org.egov.lcms.models;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds information about the advocate assigned to particular summon/warrant refernce no
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalCaseAdvocate   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("refernceNo")
  private String refernceNo = null;

  @JsonProperty("advocateName")
  private String advocateName = null;

  @JsonProperty("assignedDate")
  private Long assignedDate = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
}

