package org.egov.works.workorder.persistence.helper;

import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.WorkOrder;
import org.egov.works.workorder.web.contract.WorksStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WorkOrderHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptance")
    private String letterOfAcceptance = null;

    @JsonProperty("workOrderDate")
    private Long workOrderDate = null;

    @JsonProperty("workOrderNumber")
    private String workOrderNumber = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("stateId")
    private String stateId = null;
    
    private String remarks = null;


    public WorkOrder toDomain() {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setId(this.id);
        workOrder.setTenantId(this.tenantId);
        workOrder.setStateId(this.stateId);
        LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
        letterOfAcceptance.setId(this.letterOfAcceptance);
        workOrder.setLetterOfAcceptance(letterOfAcceptance);
        workOrder.setWorkOrderNumber(this.workOrderNumber);
        workOrder.setWorkOrderDate(this.workOrderDate);
        WorksStatus worksStatus = new WorksStatus();
        worksStatus.setCode(this.status);
        workOrder.setStatus(worksStatus);
        workOrder.setRemarks(this.remarks);
        return workOrder;
    }
}
