

package org.egov.lams.common.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An object which holds the  usage Type Master info
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageType {

   @NotNull
  private Long id = null;

  @NotNull
  @Size(min = 2, max = 128)
  private String tenantId = null;

  @NotNull
  @Size(min = 1, max = 64)
  private String code = null;

  @NotNull
  @Size(min = 1, max = 128)
  private String name = null;

  @NotNull
  private Boolean isActive = null;
  
}

