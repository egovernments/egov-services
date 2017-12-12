package org.egov.works.masters.web.contract;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ramki on 4/11/17.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduleOfRateSearchCriteria {
    @NotNull
    private String tenantId;
    private List<String> ids;
    private List<String> sorCodes;
    private List<String> scheduleCategoryCodes;
    private Long validSORRateDate;
    private Long validMarketRateDate;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean isUpdateUniqueCheck = Boolean.FALSE;
}
