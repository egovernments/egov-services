package org.egov.works.measurementbook.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Transaction  details of asset
 */
@ApiModel(description = "Transaction  details of asset")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class TransactionHistory   {
  @JsonProperty("transactionDate")
  private Long transactionDate = null;

  @JsonProperty("transactionAmount")
  private BigDecimal transactionAmount = null;

  @JsonProperty("valueBeforeTransaction")
  private BigDecimal valueBeforeTransaction = null;

  @JsonProperty("valueAfterTransaction")
  private BigDecimal valueAfterTransaction = null;

  /**
   * Transaction type revaluation or depreciation. value as .
   */
  public enum TransactionTypeEnum {
    REVALUATION("REVALUATION"),
    
    DEPRECIATION("DEPRECIATION");

    private String value;

    TransactionTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransactionTypeEnum fromValue(String text) {
      for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("transactionType")
  private TransactionTypeEnum transactionType = null;

  public TransactionHistory transactionDate(Long transactionDate) {
    this.transactionDate = transactionDate;
    return this;
  }

   /**
   * transaction date of the asset
   * @return transactionDate
  **/
  @ApiModelProperty(value = "transaction date of the asset")


  public Long getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Long transactionDate) {
    this.transactionDate = transactionDate;
  }

  public TransactionHistory transactionAmount(BigDecimal transactionAmount) {
    this.transactionAmount = transactionAmount;
    return this;
  }

   /**
   * transaction amount of the asset
   * @return transactionAmount
  **/
  @ApiModelProperty(value = "transaction amount of the asset")

  @Valid

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(BigDecimal transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public TransactionHistory valueBeforeTransaction(BigDecimal valueBeforeTransaction) {
    this.valueBeforeTransaction = valueBeforeTransaction;
    return this;
  }

   /**
   * value of the asset before transaction
   * @return valueBeforeTransaction
  **/
  @ApiModelProperty(value = "value of the asset before transaction")

  @Valid

  public BigDecimal getValueBeforeTransaction() {
    return valueBeforeTransaction;
  }

  public void setValueBeforeTransaction(BigDecimal valueBeforeTransaction) {
    this.valueBeforeTransaction = valueBeforeTransaction;
  }

  public TransactionHistory valueAfterTransaction(BigDecimal valueAfterTransaction) {
    this.valueAfterTransaction = valueAfterTransaction;
    return this;
  }

   /**
   * value of the asset after transaction
   * @return valueAfterTransaction
  **/
  @ApiModelProperty(value = "value of the asset after transaction")

  @Valid

  public BigDecimal getValueAfterTransaction() {
    return valueAfterTransaction;
  }

  public void setValueAfterTransaction(BigDecimal valueAfterTransaction) {
    this.valueAfterTransaction = valueAfterTransaction;
  }

  public TransactionHistory transactionType(TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
    return this;
  }

   /**
   * Transaction type revaluation or depreciation. value as .
   * @return transactionType
  **/
  @ApiModelProperty(value = "Transaction type revaluation or depreciation. value as .")


  public TransactionTypeEnum getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionHistory transactionHistory = (TransactionHistory) o;
    return Objects.equals(this.transactionDate, transactionHistory.transactionDate) &&
        Objects.equals(this.transactionAmount, transactionHistory.transactionAmount) &&
        Objects.equals(this.valueBeforeTransaction, transactionHistory.valueBeforeTransaction) &&
        Objects.equals(this.valueAfterTransaction, transactionHistory.valueAfterTransaction) &&
        Objects.equals(this.transactionType, transactionHistory.transactionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionDate, transactionAmount, valueBeforeTransaction, valueAfterTransaction, transactionType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionHistory {\n");
    
    sb.append("    transactionDate: ").append(toIndentedString(transactionDate)).append("\n");
    sb.append("    transactionAmount: ").append(toIndentedString(transactionAmount)).append("\n");
    sb.append("    valueBeforeTransaction: ").append(toIndentedString(valueBeforeTransaction)).append("\n");
    sb.append("    valueAfterTransaction: ").append(toIndentedString(valueAfterTransaction)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
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

