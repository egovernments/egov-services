package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Year wise depreciation details of asset
 */
@ApiModel(description = "Year wise depreciation details of asset")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class YearWiseDepreciation   {
  @JsonProperty("depreciationRate")
  private Integer depreciationRate = null;

  @JsonProperty("financialYear")
  private String financialYear = null;

  @JsonProperty("assetId")
  private Long assetId = null;

  @JsonProperty("usefulLifeInYears")
  private Long usefulLifeInYears = null;

  public YearWiseDepreciation depreciationRate(Integer depreciationRate) {
    this.depreciationRate = depreciationRate;
    return this;
  }

   /**
   * Rate of Depreciation for asset , It will be a float value.
   * @return depreciationRate
  **/
  @ApiModelProperty(required = true, value = "Rate of Depreciation for asset , It will be a float value.")
  @NotNull


  public Integer getDepreciationRate() {
    return depreciationRate;
  }

  public void setDepreciationRate(Integer depreciationRate) {
    this.depreciationRate = depreciationRate;
  }

  public YearWiseDepreciation financialYear(String financialYear) {
    this.financialYear = financialYear;
    return this;
  }

   /**
   * Required if depreciation rate is specified,Options are the list of financial years.
   * @return financialYear
  **/
  @ApiModelProperty(required = true, value = "Required if depreciation rate is specified,Options are the list of financial years.")
  @NotNull


  public String getFinancialYear() {
    return financialYear;
  }

  public void setFinancialYear(String financialYear) {
    this.financialYear = financialYear;
  }

  public YearWiseDepreciation assetId(Long assetId) {
    this.assetId = assetId;
    return this;
  }

   /**
   * id of the asset for which depreciation is required.
   * @return assetId
  **/
  @ApiModelProperty(required = true, value = "id of the asset for which depreciation is required.")
  @NotNull


  public Long getAssetId() {
    return assetId;
  }

  public void setAssetId(Long assetId) {
    this.assetId = assetId;
  }

  public YearWiseDepreciation usefulLifeInYears(Long usefulLifeInYears) {
    this.usefulLifeInYears = usefulLifeInYears;
    return this;
  }

   /**
   * Calculated value based on the percentage.
   * @return usefulLifeInYears
  **/
  @ApiModelProperty(value = "Calculated value based on the percentage.")


  public Long getUsefulLifeInYears() {
    return usefulLifeInYears;
  }

  public void setUsefulLifeInYears(Long usefulLifeInYears) {
    this.usefulLifeInYears = usefulLifeInYears;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    YearWiseDepreciation yearWiseDepreciation = (YearWiseDepreciation) o;
    return Objects.equals(this.depreciationRate, yearWiseDepreciation.depreciationRate) &&
        Objects.equals(this.financialYear, yearWiseDepreciation.financialYear) &&
        Objects.equals(this.assetId, yearWiseDepreciation.assetId) &&
        Objects.equals(this.usefulLifeInYears, yearWiseDepreciation.usefulLifeInYears);
  }

  @Override
  public int hashCode() {
    return Objects.hash(depreciationRate, financialYear, assetId, usefulLifeInYears);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class YearWiseDepreciation {\n");
    
    sb.append("    depreciationRate: ").append(toIndentedString(depreciationRate)).append("\n");
    sb.append("    financialYear: ").append(toIndentedString(financialYear)).append("\n");
    sb.append("    assetId: ").append(toIndentedString(assetId)).append("\n");
    sb.append("    usefulLifeInYears: ").append(toIndentedString(usefulLifeInYears)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

