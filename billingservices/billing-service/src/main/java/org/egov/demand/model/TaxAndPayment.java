package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TaxAndPayment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxAndPayment   {
  @JsonProperty("businessService")
  private String businessService;

  @JsonProperty("taxAmount")
  private BigDecimal taxAmount;

  @JsonProperty("amountPaid")
  private BigDecimal amountPaid;

  public TaxAndPayment businessService(String businessService) {
    this.businessService = businessService;
    return this;
  }

  /**
   * unique code of the business service
   * @return businessService
  **/

  public String getBusinessService() {
    return businessService;
  }

  public void setBusinessService(String businessService) {
    this.businessService = businessService;
  }

  public TaxAndPayment taxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
    return this;
  }

  /**
   * amount to be paid by the payer
   * @return taxAmount
  **/
  @Valid

  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public TaxAndPayment amountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
    return this;
  }

  /**
   * amount paid by the payer
   * @return amountPaid
  **/
  @Valid

  public BigDecimal getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(BigDecimal amountPaid) {
    this.amountPaid = amountPaid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaxAndPayment taxAndPayment = (TaxAndPayment) o;
    return Objects.equals(this.businessService, taxAndPayment.businessService) &&
        Objects.equals(this.taxAmount, taxAndPayment.taxAmount) &&
        Objects.equals(this.amountPaid, taxAndPayment.amountPaid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(businessService, taxAmount, amountPaid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaxAndPayment {\n");
    
    sb.append("    businessService: ").append(toIndentedString(businessService)).append("\n");
    sb.append("    taxAmount: ").append(toIndentedString(taxAmount)).append("\n");
    sb.append("    amountPaid: ").append(toIndentedString(amountPaid)).append("\n");
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

