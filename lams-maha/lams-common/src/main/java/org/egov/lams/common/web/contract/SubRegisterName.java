
package org.egov.lams.common.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An object which holds the Register Name Master info
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubRegisterName {
  @NotNull
  private Long id = null;

  @NotNull
  @Size(min = 2, max = 128)
  private String tenantId;

  @NotNull
  @Size(min = 1, max = 64)
  private String code = null;

  @NotNull
  private Long registerName = null;
  
  private String subRegister=null;

  @NotNull
  private Boolean isactive = null;
}

