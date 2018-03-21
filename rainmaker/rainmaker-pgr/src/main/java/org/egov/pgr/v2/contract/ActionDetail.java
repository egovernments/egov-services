package org.egov.pgr.v2.contract;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Media object to encapsulate uploaded media (photo, doc, video)
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ActionDetail   {
  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("by")
  private String by = null;

  @JsonProperty("when")
  private Long when = null;

  public ActionDetail uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  /**
   * GUID id.
   * @return uuid
  **/


  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public ActionDetail by(String by) {
    this.by = by;
    return this;
  }

  /**
   * who made the Action Citizen/Employee, it's a combination of userid and role delimited by colon (ex- userid:citizen).
   * @return by
  **/


  public String getBy() {
    return by;
  }

  public void setBy(String by) {
    this.by = by;
  }

  public ActionDetail when(Long when) {
    this.when = when;
    return this;
  }

  /**
   * epoch time of when the action made.
   * @return when
  **/

  public Long getWhen() {
    return when;
  }

  public void setWhen(Long when) {
    this.when = when;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionDetail actionDetail = (ActionDetail) o;
    return Objects.equals(this.uuid, actionDetail.uuid) &&
        Objects.equals(this.by, actionDetail.by) &&
        Objects.equals(this.when, actionDetail.when);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, by, when);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionDetail {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    by: ").append(toIndentedString(by)).append("\n");
    sb.append("    when: ").append(toIndentedString(when)).append("\n");
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

