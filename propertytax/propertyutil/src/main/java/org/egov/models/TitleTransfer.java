package org.egov.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransfer {
	
	private Long id = null;
    
    @Valid
    @NotNull
    private List<User> newOwners;

    @NotNull
    private String upicNo;

    @NotNull
    private String transferReason;

    @Size(min = 1, max = 15)
    private String registrationDocNo;

    private String registrationDocDate;

    @NotNull
    private Double departmentGuidelineValue;

    private Double partiesConsiderationValue;

    private Long courtOrderNumber;

    @Size(min = 1, max = 15)
    private String subRegOfficeName;

    private Double titleTrasferFee;

    private List<Document> documents;

    private Address correspondenceAddress;

    private String stateId;

    private String receiptnumber;

    private String receiptdate;

    private AuditDetails auditDetails;

    private WorkFlowDetails workFlowDetails;
    
    private String applicationNo;
    
    @NotNull
    private String tenantId;
}
