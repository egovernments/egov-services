package org.egov.pgr.v2.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Assignee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-03-19T06:08:09.296Z")

public class Assignee extends ActionDetail  {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("assigneeType")
  private String assigneeType = null;

  public Assignee id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Either of employee/designation/agency/authority this request is assigned to
   * @return id
  **/
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Assignee assigneeType(String assigneeType) {
    this.assigneeType = assigneeType;
    return this;
  }

  /**
   * This will indecate that wether assignee is an employee or all employee of perticular department or  employee of perticular position
   * @return assigneeType
  **/


  public String getAssigneeType() {
    return assigneeType;
  }

  public void setAssigneeType(String assigneeType) {
    this.assigneeType = assigneeType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Assignee assignee = (Assignee) o;
    return Objects.equals(this.id, assignee.id) &&
        Objects.equals(this.assigneeType, assignee.assigneeType) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, assigneeType, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Assignee {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    assigneeType: ").append(toIndentedString(assigneeType)).append("\n");
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

