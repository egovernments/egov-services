package org.egov.pgr.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Media object to encapsulate uploaded media (photo, doc, video)
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-03-01T09:34:47.978Z")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("by")
  private String by = null;

  @JsonProperty("when")
  private Long when = null;

  @JsonProperty("url")
  private String url = null;

  public Media id(String id) {
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

  public Media by(String by) {
    this.by = by;
    return this;
  }

   /**
   * who uploaded/attached this media(combination of userId and role separated by -).
   * @return by
  **/

  public String getBy() {
    return by;
  }

  public void setBy(String by) {
    this.by = by;
  }

  public Media when(Long when) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Media media = (Media) o;
    return Objects.equals(this.id, media.id) &&
        Objects.equals(this.by, media.by) &&
        Objects.equals(this.when, media.when) &&
        Objects.equals(this.url, media.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, by, when, url);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Media {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    by: ").append(toIndentedString(by)).append("\n");
    sb.append("    when: ").append(toIndentedString(when)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

