package org.egov.works.estimate.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbstractEstimateAssetDetailSearchContract {

    @NotNull
    private String tenantId;
    private List<String> ids;
    private List<String> abstractEstimateIds;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
}
