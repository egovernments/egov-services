package org.egov.works.estimate.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class TechnicalSanctionSearchContract {

    @NotNull
    private String tenantId;
    private List<String> ids;
    private List<String> technicalSanctionNumbers;
    private List<String> adminSanctionNumbers;
    private List<String> detailedEstimateIds;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
}
