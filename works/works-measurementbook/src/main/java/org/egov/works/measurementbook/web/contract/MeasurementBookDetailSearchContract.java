package org.egov.works.measurementbook.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MeasurementBookDetailSearchContract {

    private List<String> ids;

    private String tenantId;

    private List<String> measurementBookIds;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
}
