package org.egov.pgr.v2.contract;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Status
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status extends ActionDetail  {
	
  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("status")
  private String status = null;

  public Status uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  
  /**
   * Either of employee/designation/agency/authority this request is assigned to
   * @return uuid
  **/
  @NotNull


  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Status status(String status) {
    this.status = status;
    return this;
  }

  /**
   * This will indecate that wether assignee is an employee or all employee of perticular department or  employee of perticular position
   * @return status
  **/


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
    Status status = (Status) o;
    return Objects.equals(this.uuid, status.uuid) &&
        Objects.equals(this.status, status.status) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, status, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Status {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
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

