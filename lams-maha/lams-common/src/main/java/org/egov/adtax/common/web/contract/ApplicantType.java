

package org.egov.adtax.common.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An object which holds the  ApplicantType Master info
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantType {

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

