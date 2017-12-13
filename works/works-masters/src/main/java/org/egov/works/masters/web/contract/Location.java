package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Hold the asset location specific information.
 */
@ApiModel(description = "Hold the asset location specific information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class Location   {
  @JsonProperty("locality")
  private Long locality = null;

  @JsonProperty("zone")
  private Long zone = null;

  @JsonProperty("revenueWard")
  private Long revenueWard = null;

  @JsonProperty("block")
  private Long block = null;

  @JsonProperty("street")
  private Long street = null;

  @JsonProperty("electionWard")
  private Long electionWard = null;

  @JsonProperty("doorNo")
  private String doorNo = null;

  @JsonProperty("pinCode")
  private Long pinCode = null;

  public Location locality(Long locality) {
    this.locality = locality;
    return this;
  }

   /**
   * Boundary location id.
   * @return locality
  **/
  @ApiModelProperty(value = "Boundary location id.")


  public Long getLocality() {
    return locality;
  }

  public void setLocality(Long locality) {
    this.locality = locality;
  }

  public Location zone(Long zone) {
    this.zone = zone;
    return this;
  }

   /**
   * Boundary zone id.
   * @return zone
  **/
  @ApiModelProperty(value = "Boundary zone id.")


  public Long getZone() {
    return zone;
  }

  public void setZone(Long zone) {
    this.zone = zone;
  }

  public Location revenueWard(Long revenueWard) {
    this.revenueWard = revenueWard;
    return this;
  }

   /**
   * Boundary revenueward id.
   * @return revenueWard
  **/
  @ApiModelProperty(value = "Boundary revenueward id.")


  public Long getRevenueWard() {
    return revenueWard;
  }

  public void setRevenueWard(Long revenueWard) {
    this.revenueWard = revenueWard;
  }

  public Location block(Long block) {
    this.block = block;
    return this;
  }

   /**
   * Boundary blick id.
   * @return block
  **/
  @ApiModelProperty(value = "Boundary blick id.")


  public Long getBlock() {
    return block;
  }

  public void setBlock(Long block) {
    this.block = block;
  }

  public Location street(Long street) {
    this.street = street;
    return this;
  }

   /**
   * Boundary street id.
   * @return street
  **/
  @ApiModelProperty(value = "Boundary street id.")


  public Long getStreet() {
    return street;
  }

  public void setStreet(Long street) {
    this.street = street;
  }

  public Location electionWard(Long electionWard) {
    this.electionWard = electionWard;
    return this;
  }

   /**
   * Boundary election ward id.
   * @return electionWard
  **/
  @ApiModelProperty(value = "Boundary election ward id.")


  public Long getElectionWard() {
    return electionWard;
  }

  public void setElectionWard(Long electionWard) {
    this.electionWard = electionWard;
  }

  public Location doorNo(String doorNo) {
    this.doorNo = doorNo;
    return this;
  }

   /**
   * Boundary door number.
   * @return doorNo
  **/
  @ApiModelProperty(value = "Boundary door number.")


  public String getDoorNo() {
    return doorNo;
  }

  public void setDoorNo(String doorNo) {
    this.doorNo = doorNo;
  }

  public Location pinCode(Long pinCode) {
    this.pinCode = pinCode;
    return this;
  }

   /**
   * pin code of the location.
   * @return pinCode
  **/
  @ApiModelProperty(value = "pin code of the location.")


  public Long getPinCode() {
    return pinCode;
  }

  public void setPinCode(Long pinCode) {
    this.pinCode = pinCode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.locality, location.locality) &&
        Objects.equals(this.zone, location.zone) &&
        Objects.equals(this.revenueWard, location.revenueWard) &&
        Objects.equals(this.block, location.block) &&
        Objects.equals(this.street, location.street) &&
        Objects.equals(this.electionWard, location.electionWard) &&
        Objects.equals(this.doorNo, location.doorNo) &&
        Objects.equals(this.pinCode, location.pinCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locality, zone, revenueWard, block, street, electionWard, doorNo, pinCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    
    sb.append("    locality: ").append(toIndentedString(locality)).append("\n");
    sb.append("    zone: ").append(toIndentedString(zone)).append("\n");
    sb.append("    revenueWard: ").append(toIndentedString(revenueWard)).append("\n");
    sb.append("    block: ").append(toIndentedString(block)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    electionWard: ").append(toIndentedString(electionWard)).append("\n");
    sb.append("    doorNo: ").append(toIndentedString(doorNo)).append("\n");
    sb.append("    pinCode: ").append(toIndentedString(pinCode)).append("\n");
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

