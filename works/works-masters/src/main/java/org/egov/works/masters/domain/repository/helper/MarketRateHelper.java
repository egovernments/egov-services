package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.MarketRate;

import java.math.BigDecimal;

/**
 * Created by ramki on 6/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketRateHelper {
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

    public MarketRate toDomain() {
        MarketRate marketRate = new MarketRate();
        marketRate.setId(this.id);
        marketRate.setTenantId(this.tenantId);
        marketRate.setScheduleOfRate(this.scheduleOfRate);
        marketRate.setFromDate(this.fromDate);
        marketRate.setToDate(this.toDate);
        marketRate.setRate(this.rate);
        return marketRate;
    }
}
