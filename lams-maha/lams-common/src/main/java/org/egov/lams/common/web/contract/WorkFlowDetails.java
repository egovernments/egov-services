package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds info about the assignee and other workflow related info
 */
@ApiModel(description = "This object holds info about the assignee and other workflow related info")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class WorkFlowDetails   {
  @JsonProperty("department")
  private String department = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("assignee")
  private Long assignee = null;

  @JsonProperty("action")
  private String action = null;

  @JsonProperty("status")
  private String status = null;

  public WorkFlowDetails department(String department) {
    this.department = department;
    return this;
  }

   /**
   * Selected department.
   * @return department
  **/
  @ApiModelProperty(value = "Selected department.")


  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public WorkFlowDetails designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Selected designation.
   * @return designation
  **/
  @ApiModelProperty(value = "Selected designation.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public WorkFlowDetails assignee(Long assignee) {
    this.assignee = assignee;
    return this;
  }

   /**
   * Selected assignee.
   * @return assignee
  **/
  @ApiModelProperty(value = "Selected assignee.")


  public Long getAssignee() {
    return assignee;
  }

  public void setAssignee(Long assignee) {
    this.assignee = assignee;
  }

  public WorkFlowDetails action(String action) {
    this.action = action;
    return this;
  }

   /**
   * chosen action.
   * @return action
  **/
  @ApiModelProperty(value = "chosen action.")


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public WorkFlowDetails status(String status) {
    this.status = status;
    return this;
  }

   /**
   * current status.
   * @return status
  **/
  @ApiModelProperty(value = "current status.")


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkFlowDetails workFlowDetails = (WorkFlowDetails) o;
    return Objects.equals(this.department, workFlowDetails.department) &&
        Objects.equals(this.designation, workFlowDetails.designation) &&
        Objects.equals(this.assignee, workFlowDetails.assignee) &&
        Objects.equals(this.action, workFlowDetails.action) &&
        Objects.equals(this.status, workFlowDetails.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(department, designation, assignee, action, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkFlowDetails {\n");
    
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
    sb.append("    assignee: ").append(toIndentedString(assignee)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

