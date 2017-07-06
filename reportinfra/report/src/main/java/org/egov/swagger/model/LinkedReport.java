package org.egov.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;


public class LinkedReport   {
  @JsonProperty("reportName")
  private String reportName = null;

  /**
   * Gets or Sets linkType
   */
  public enum LinkTypeEnum {
    COLUMN("COLUMN"),
    
    COLUMNVALUE("COLUMNVALUE"),
    
    FUXEDURL("FUXEDURL");

    private String value;

    LinkTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static LinkTypeEnum fromValue(String text) {
      for (LinkTypeEnum b : LinkTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("linkType")
  private LinkTypeEnum linkType = null;

  public LinkedReport reportName(String reportName) {
    this.reportName = reportName;
    return this;
  }

   /**
   * name of the next report
   * @return reportName
  **/
  
  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  public LinkedReport linkType(LinkTypeEnum linkType) {
    this.linkType = linkType;
    return this;
  }

   /**
   * Get linkType
   * @return linkType
  **/
  
  public LinkTypeEnum getLinkType() {
    return linkType;
  }

  public void setLinkType(LinkTypeEnum linkType) {
    this.linkType = linkType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkedReport linkedReport = (LinkedReport) o;
    return Objects.equals(this.reportName, linkedReport.reportName) &&
        Objects.equals(this.linkType, linkedReport.linkType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportName, linkType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkedReport {\n");
    
    sb.append("    reportName: ").append(toIndentedString(reportName)).append("\n");
    sb.append("    linkType: ").append(toIndentedString(linkType)).append("\n");
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

