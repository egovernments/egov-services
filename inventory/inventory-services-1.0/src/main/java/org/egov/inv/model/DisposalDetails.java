package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.ScrapDetails;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object contains the list of materials marked as scraped in the selected store and are to be disposed.   
 */
@ApiModel(description = "This object contains the list of materials marked as scraped in the selected store and are to be disposed.   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class DisposalDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("scrapDetails")
  private ScrapDetails scrapDetails = null;

  @JsonProperty("disposalQuantity")
  private BigDecimal disposalQuantity = null;

  @JsonProperty("disposalValue")
  private Double disposalValue = null;

  public DisposalDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Disposal Details 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Disposal Details ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DisposalDetails scrapDetails(ScrapDetails scrapDetails) {
    this.scrapDetails = scrapDetails;
    return this;
  }

   /**
   * Get scrapDetails
   * @return scrapDetails
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public ScrapDetails getScrapDetails() {
    return scrapDetails;
  }

  public void setScrapDetails(ScrapDetails scrapDetails) {
    this.scrapDetails = scrapDetails;
  }

  public DisposalDetails disposalQuantity(BigDecimal disposalQuantity) {
    this.disposalQuantity = disposalQuantity;
    return this;
  }

   /**
   * disposal quantity of the DisposalDetails 
   * @return disposalQuantity
  **/
  @ApiModelProperty(required = true, value = "disposal quantity of the DisposalDetails ")
  @NotNull

  @Valid

  public BigDecimal getDisposalQuantity() {
    return disposalQuantity;
  }

  public void setDisposalQuantity(BigDecimal disposalQuantity) {
    this.disposalQuantity = disposalQuantity;
  }

  public DisposalDetails disposalValue(Double disposalValue) {
    this.disposalValue = disposalValue;
    return this;
  }

   /**
   * disposal value of the DisposalDetails 
   * @return disposalValue
  **/
  @ApiModelProperty(required = true, value = "disposal value of the DisposalDetails ")
  @NotNull


  public Double getDisposalValue() {
    return disposalValue;
  }

  public void setDisposalValue(Double disposalValue) {
    this.disposalValue = disposalValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisposalDetails disposalDetails = (DisposalDetails) o;
    return Objects.equals(this.id, disposalDetails.id) &&
        Objects.equals(this.scrapDetails, disposalDetails.scrapDetails) &&
        Objects.equals(this.disposalQuantity, disposalDetails.disposalQuantity) &&
        Objects.equals(this.disposalValue, disposalDetails.disposalValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, scrapDetails, disposalQuantity, disposalValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisposalDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    scrapDetails: ").append(toIndentedString(scrapDetails)).append("\n");
    sb.append("    disposalQuantity: ").append(toIndentedString(disposalQuantity)).append("\n");
    sb.append("    disposalValue: ").append(toIndentedString(disposalValue)).append("\n");
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

