package org.egov.pa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object which holds the list of KPI Values for each tenant or financial year
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T06:28:54.643Z")

public class KpiValueList   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("kpiCode")
  private String kpiCode = null;

  @JsonProperty("finYear")
  private String finYear = null;

  @JsonProperty("kpiTarget")
  private KpiTarget kpiTarget = null;

  @JsonProperty("kpiValue")
  private List<KpiValue> kpiValue = null;

  public KpiValueList tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public KpiValueList kpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
    return this;
  }

   /**
   * unique identifier of a KPI object
   * @return kpiCode
  **/

 @Pattern(regexp="^[a-zA-Z0-9 ]+$")
  public String getKpiCode() {
    return kpiCode;
  }

  public void setKpiCode(String kpiCode) {
    this.kpiCode = kpiCode;
  }

  public KpiValueList finYear(String finYear) {
    this.finYear = finYear;
    return this;
  }

   /**
   * Financial Year for which the KPI is applicable
   * @return finYear
  **/

 @Pattern(regexp="^[a-zA-Z0-9-]+$")
  public String getFinYear() {
    return finYear;
  }

  public void setFinYear(String finYear) {
    this.finYear = finYear;
  }

  public KpiValueList kpiTarget(KpiTarget kpiTarget) {
    this.kpiTarget = kpiTarget;
    return this;
  }

   /**
   * Get kpiTarget
   * @return kpiTarget
  **/

  @Valid

  public KpiTarget getKpiTarget() {
    return kpiTarget;
  }

  public void setKpiTarget(KpiTarget kpiTarget) {
    this.kpiTarget = kpiTarget;
  }

  public KpiValueList kpiValue(List<KpiValue> kpiValue) {
    this.kpiValue = kpiValue;
    return this;
  }

  public KpiValueList addKpiValueItem(KpiValue kpiValueItem) {
    if (this.kpiValue == null) {
      this.kpiValue = new ArrayList<KpiValue>();
    }
    this.kpiValue.add(kpiValueItem);
    return this;
  }

   /**
   * Get kpiValue
   * @return kpiValue
  **/

  @Valid

  public List<KpiValue> getKpiValue() {
    return kpiValue;
  }

  public void setKpiValue(List<KpiValue> kpiValue) {
    this.kpiValue = kpiValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KpiValueList kpiValueList = (KpiValueList) o;
    return Objects.equals(this.tenantId, kpiValueList.tenantId) &&
        Objects.equals(this.kpiCode, kpiValueList.kpiCode) &&
        Objects.equals(this.finYear, kpiValueList.finYear) &&
        Objects.equals(this.kpiTarget, kpiValueList.kpiTarget) &&
        Objects.equals(this.kpiValue, kpiValueList.kpiValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, kpiCode, finYear, kpiTarget, kpiValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiValueList {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    kpiCode: ").append(toIndentedString(kpiCode)).append("\n");
    sb.append("    finYear: ").append(toIndentedString(finYear)).append("\n");
    sb.append("    kpiTarget: ").append(toIndentedString(kpiTarget)).append("\n");
    sb.append("    kpiValue: ").append(toIndentedString(kpiValue)).append("\n");
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

