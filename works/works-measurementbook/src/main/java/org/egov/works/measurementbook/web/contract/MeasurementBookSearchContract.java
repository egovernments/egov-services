package org.egov.works.measurementbook.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MeasurementBookSearchContract {

    @NotNull
    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortProperty;

    private List<String> ids;

    private List<String> department;

    private Long fromDate;

    private Long toDate;

    private String createdBy;

    private List<String> workOrderNumbers; 

    private List<String> mbRefNumbers;

    private List<String> loaNumbers;

    private List<String> detailedEstimateNumbers; 
    
    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> workIdentificationNumbers;
    
    private String loaNumberLike;
    
    private String workOrderNumberLike; 

    private String mbRefNumberLike;

    private String detailedEstimateNumberLike; 
    
    private String contractorNameLike;

    private String contractorCodeLike;

    private String workIdentificationNumberLike;

    private List<String> statuses;

    private Boolean isPartRate;

    private Boolean isBillCreated;
}
