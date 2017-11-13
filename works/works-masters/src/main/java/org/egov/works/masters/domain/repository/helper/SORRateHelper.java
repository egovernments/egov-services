package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.SORRate;
import org.egov.works.masters.web.contract.ScheduleOfRate;

import java.math.BigDecimal;

/**
 * Created by ramki on 6/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SORRateHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("scheduleOfRate")
    private String scheduleOfRate = null;

    @JsonProperty("fromDate")
    private Long fromDate = null;

    @JsonProperty("toDate")
    private Long toDate = null;

    @JsonProperty("rate")
    private BigDecimal rate = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public SORRate toDomain() {
        SORRate sorRate = new SORRate();
        sorRate.setId(this.id);
        sorRate.setTenantId(this.tenantId);
        sorRate.setScheduleOfRate(this.scheduleOfRate);
        sorRate.setFromDate(this.fromDate);
        sorRate.setToDate(this.toDate);
        sorRate.setRate(this.rate);
        return sorRate;
    }
}
