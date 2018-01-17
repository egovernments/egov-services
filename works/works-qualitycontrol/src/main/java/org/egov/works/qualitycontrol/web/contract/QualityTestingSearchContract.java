package org.egov.works.qualitycontrol.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityTestingSearchContract {

    @NotNull
    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> workOrderNumbers;

    private String workOrderNumberLike;

    private List<String> ids;

    private List<String> loaNumbers;

    private String loaNumberLike;

    private List<String> detailedEstimateNumbers;

    private String detailedEstimateNumberLike;

    private List<String> workIdentificationNumbers;

    private String workIdentificationNumberLike;

    private Long fromDate;

    private Long toDate;

}
