package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.works.workorder.web.contract.WorkOrder;
import org.egov.works.workorder.web.contract.WorkOrderDetail;

@Data
public class WorkOrderDetailHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("workOrder")
    private String workOrder = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("editable")
    private Boolean editable = null;

    public WorkOrderDetail toDomain() {

        WorkOrderDetail workOrderDetail = new WorkOrderDetail();
        WorkOrder workOrder = new WorkOrder();
        workOrderDetail.setId(this.id);
        workOrderDetail.setTenantId(this.tenantId);
        workOrderDetail.setRemarks(this.remarks);
        workOrderDetail.setEditable(this.editable);
        workOrderDetail.setWorkOrder(this.workOrder);

        return workOrderDetail;
    }

}
