package org.egov.lcms.models;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Collection of audit related fields used by most models
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditDetails   {
  @JsonProperty("createdBy")
  private String createdBy = null;

  @JsonProperty("lastModifiedBy")
  private String lastModifiedBy = null;

  @JsonProperty("createdTime")
  private BigDecimal createdTime = null;

  @JsonProperty("lastModifiedTime")
  private BigDecimal lastModifiedTime = null; 
}

