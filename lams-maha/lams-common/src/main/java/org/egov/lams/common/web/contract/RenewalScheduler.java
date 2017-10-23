
package org.egov.lams.common.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An object which holds the  Renewal Schedule Type Master info
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalScheduler {
  @NotNull
  private Long id = null;

  @NotNull
  @Size(min = 2, max = 128)
  private String tenantId = null;

  @NotNull
  @Size(min = 1, max = 64)
  private String licenseType = null;

  @NotNull
  @Size(min = 1, max = 64)
  private String licenseSubType = null;

  @NotNull
  private Long fromDate = null;

  @NotNull
  private Long toDate = null;

  @NotNull
  @Size(min = 1, max = 64)
  private String licensePeriod = null;

  @NotNull
  private Long startDate = null;

  @NotNull
  private Long startMonth = null;

  @NotNull
  private Long endDate = null;

  @NotNull
  private Long endMonth = null;

  @NotNull
  private Long period = null;

  @NotNull
  private Long noOfDaysPerYear = null;

}

