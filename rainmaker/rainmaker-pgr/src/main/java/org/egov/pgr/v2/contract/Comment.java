package org.egov.pgr.v2.contract;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Comment
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends ActionDetail  {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("isInternal")
  private Boolean isInternal = null;

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
   * if true then it is not return in search result to the citizen
   * @return isInternal
  **/


  public Boolean isIsInternal() {
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
    return Objects.equals(this.message, comment.message) &&
        Objects.equals(this.isInternal, comment.isInternal) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, isInternal, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

