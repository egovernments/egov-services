package org.egov.pgr.v2.contract;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Media
 */
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media extends ActionDetail  {
  @JsonProperty("url")
  private String url = null;

  @JsonProperty("isInternal")
  private String isInternal = null;

  public Media url(String url) {
    this.url = url;
    return this;
  }

  /**
   * URL for the media
   * @return url
  **/


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Media isInternal(String isInternal) {
    this.isInternal = isInternal;
    return this;
  }

  /**
   * if true then it is not return in search result to the citizen.
   * @return isInternal
  **/


  public String getIsInternal() {
    return isInternal;
  }

  public void setIsInternal(String isInternal) {
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
    Media media = (Media) o;
    return Objects.equals(this.url, media.url) &&
        Objects.equals(this.isInternal, media.isInternal) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, isInternal, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Media {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

