package org.egov.pgr.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


/**
 * Comment object to encapsulate comment from Citizen OR Employee
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-03-01T09:34:47.978Z")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("by")
  private String by = null;

  @JsonProperty("when")
  private Long when = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("isInternal")
  private Boolean isInternal = null;

  public Comment id(String id) {
    this.id = id;
    return this;
  }

   /**
   * GUID for the comment table.
   * @return id
  **/

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Comment by(String by) {
    this.by = by;
    return this;
  }

   /**
   * who made the commet Citizen/Employee(combination of userId and role separated by -).
   * @return by
  **/

  public String getBy() {
    return by;
  }

  public void setBy(String by) {
    this.by = by;
  }

  public Comment when(Long when) {
    this.when = when;
    return this;
  }

   /**
   * epoch time of when the comment was created
   * @return when
  **/
  public Long getWhen() {
    return when;
  }

  public void setWhen(Long when) {
    this.when = when;
  }

  public Comment message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Comment message.
   * @return message
  **/

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Comment isInternal(Boolean isInternal) {
    this.isInternal = isInternal;
    return this;
  }

   /**
   * represents whether the comment has been added by employee or citizen.
   * @return isInternal
  **/

  public Boolean getIsInternal() {
    return isInternal;
  }

  public void setIsInternal(Boolean isInternal) {
    this.isInternal = isInternal;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.id, comment.id) &&
        Objects.equals(this.by, comment.by) &&
        Objects.equals(this.when, comment.when) &&
        Objects.equals(this.message, comment.message) &&
        Objects.equals(this.isInternal, comment.isInternal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, by, when, message, isInternal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    by: ").append(toIndentedString(by)).append("\n");
    sb.append("    when: ").append(toIndentedString(when)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    isInternal: ").append(toIndentedString(isInternal)).append("\n");
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

