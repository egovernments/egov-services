package org.egov.works.measurementbook.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.works.measurementbook.web.contract.MBMeasurementSheetSearchContract.MBMeasurementSheetSearchContractBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MBForContractorBillSearchContract {
    @NotNull
    private String tenantId;
    private List<String> ids;
    private String measurementBook;
    private String contractorBill;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
}
