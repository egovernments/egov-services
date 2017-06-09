package org.egov.models;

import java.util.Objects;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * OwnerInfo
 * Author : Narendra
 */

public class OwnerInfo extends User  {
  @JsonProperty("isPrimaryOwner")
  private Boolean isPrimaryOwner = null;

  @JsonProperty("isSecondaryOwner")
  private Boolean isSecondaryOwner = null;

  @JsonProperty("ownerShipPercentage")
  private Double ownerShipPercentage = null;

  @JsonProperty("ownerType")
  @Size(min=4,max=32)
  private String ownerType = null;

  public OwnerInfo isPrimaryOwner(Boolean isPrimaryOwner) {
    this.isPrimaryOwner = isPrimaryOwner;
    return this;
  }

   /**
   * The owner is primary or not
   * @return isPrimaryOwner
  **/
   public Boolean getIsPrimaryOwner() {
    return isPrimaryOwner;
  }

  public void setIsPrimaryOwner(Boolean isPrimaryOwner) {
    this.isPrimaryOwner = isPrimaryOwner;
  }

  public OwnerInfo isSecondaryOwner(Boolean isSecondaryOwner) {
    this.isSecondaryOwner = isSecondaryOwner;
    return this;
  }

   /**
   * The owner is secondary or not
   * @return isSecondaryOwner
  **/
   public Boolean getIsSecondaryOwner() {
    return isSecondaryOwner;
  }

  public void setIsSecondaryOwner(Boolean isSecondaryOwner) {
    this.isSecondaryOwner = isSecondaryOwner;
  }

  public OwnerInfo ownerShipPercentage(Double ownerShipPercentage) {
    this.ownerShipPercentage = ownerShipPercentage;
    return this;
  }

   /**
   * Ownership percentage.
   * @return ownerShipPercentage
  **/
  public Double getOwnerShipPercentage() {
    return ownerShipPercentage;
  }

  public void setOwnerShipPercentage(Double ownerShipPercentage) {
    this.ownerShipPercentage = ownerShipPercentage;
  }

  public OwnerInfo ownerType(String ownerType) {
    this.ownerType = ownerType;
    return this;
  }

   /**
   * Type of owner, based on this option rebate will be calculated.
   * @return ownerType
  **/
  public String getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(String ownerType) {
    this.ownerType = ownerType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OwnerInfo ownerInfo = (OwnerInfo) o;
    return Objects.equals(this.isPrimaryOwner, ownerInfo.isPrimaryOwner) &&
        Objects.equals(this.isSecondaryOwner, ownerInfo.isSecondaryOwner) &&
        Objects.equals(this.ownerShipPercentage, ownerInfo.ownerShipPercentage) &&
        Objects.equals(this.ownerType, ownerInfo.ownerType) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isPrimaryOwner, isSecondaryOwner, ownerShipPercentage, ownerType, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OwnerInfo {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    isPrimaryOwner: ").append(toIndentedString(isPrimaryOwner)).append("\n");
    sb.append("    isSecondaryOwner: ").append(toIndentedString(isSecondaryOwner)).append("\n");
    sb.append("    ownerShipPercentage: ").append(toIndentedString(ownerShipPercentage)).append("\n");
    sb.append("    ownerType: ").append(toIndentedString(ownerType)).append("\n");
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

