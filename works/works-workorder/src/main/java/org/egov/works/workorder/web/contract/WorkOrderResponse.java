package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Work Order items are used in case of search results, also multiple  Work Order item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Work Order items are used in case of search results, also multiple  Work Order item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class WorkOrderResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("workOrders")
    private List<WorkOrder> workOrders = null;

    public WorkOrderResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    /**
     * Get responseInfo
     *
     * @return responseInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public WorkOrderResponse workOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
        return this;
    }

    public WorkOrderResponse addWorkOrdersItem(WorkOrder workOrdersItem) {
        if (this.workOrders == null) {
            this.workOrders = new ArrayList<WorkOrder>();
        }
        this.workOrders.add(workOrdersItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return workOrders
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrderResponse workOrderResponse = (WorkOrderResponse) o;
        return Objects.equals(this.responseInfo, workOrderResponse.responseInfo) &&
                Objects.equals(this.workOrders, workOrderResponse.workOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, workOrders);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WorkOrderResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    workOrders: ").append(toIndentedString(workOrders)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

