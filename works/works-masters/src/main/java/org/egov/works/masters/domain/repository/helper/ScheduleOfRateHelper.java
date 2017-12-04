package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 6/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleOfRateHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("scheduleCategory")
    private String scheduleCategory = null;

    @JsonProperty("uom")
    private String uom = null;

    @JsonProperty("sorRates")
    private List<SORRate> sorRates = new ArrayList<SORRate>();

    @JsonProperty("marketRates")
    private List<MarketRate> marketRates = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public ScheduleOfRate toDomain() {
        ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
        scheduleOfRate.setId(this.id);
        scheduleOfRate.setTenantId(this.tenantId);
        scheduleOfRate.setCode(this.code);
        scheduleOfRate.setDescription(this.description);
        scheduleOfRate.setScheduleCategory(new ScheduleCategory());
        scheduleOfRate.getScheduleCategory().setCode(this.scheduleCategory);
        scheduleOfRate.setUom(new UOM());
        scheduleOfRate.getUom().setCode(this.uom);
        return scheduleOfRate;
    }
}
