package org.egov.pa.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object which holds the list of KPI Values for each tenant or financial year
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class KpiValueList   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("kpi")
  private KPI kpi = null;

  @JsonProperty("finYear")
  private String finYear = null;

  @JsonProperty("kpiValue")
  private KpiValue kpiValue = null;

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

  public KpiValueList kpi(KPI kpi) {
    this.kpi = kpi;
    return this;
  }

   /**
   * Get kpi
   * @return kpi
  **/

  @Valid

  public KPI getKpi() {
    return kpi;
  }

  public void setKpi(KPI kpi) {
    this.kpi = kpi;
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
        Objects.equals(this.kpi, kpiValueList.kpi) &&
        Objects.equals(this.finYear, kpiValueList.finYear) &&
        Objects.equals(this.kpiValue, kpiValueList.kpiValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, kpi, finYear, kpiValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KpiValueList {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    kpi: ").append(toIndentedString(kpi)).append("\n");
    sb.append("    finYear: ").append(toIndentedString(finYear)).append("\n");
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

