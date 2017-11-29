package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds Remarks Master data
 */
@ApiModel(description = "An Object that holds Remarks Master data")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:02:10.583Z")

public class Remarks   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("typeOfDocument")
  private String typeOfDocument = null;

  @JsonProperty("remarksType")
  private String remarksType = null;

  @JsonProperty("remarksDetails")
  private List<RemarksDetail> remarksDetails = new ArrayList<RemarksDetail>();

  public Remarks id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Remarks Master
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Remarks Master")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Remarks tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Remarks Master
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Remarks Master")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Remarks typeOfDocument(String typeOfDocument) {
    this.typeOfDocument = typeOfDocument;
    return this;
  }

   /**
   * Type Of Document of the Remarks Master
   * @return typeOfDocument
  **/
  @ApiModelProperty(required = true, value = "Type Of Document of the Remarks Master")
  @NotNull

 @Size(min=1,max=100)
  public String getTypeOfDocument() {
    return typeOfDocument;
  }

  public void setTypeOfDocument(String typeOfDocument) {
    this.typeOfDocument = typeOfDocument;
  }

  public Remarks remarksType(String remarksType) {
    this.remarksType = remarksType;
    return this;
  }

   /**
   * Remarks Type of the Remarks Master
   * @return remarksType
  **/
  @ApiModelProperty(required = true, value = "Remarks Type of the Remarks Master")
  @NotNull

 @Size(min=1,max=100)
  public String getRemarksType() {
    return remarksType;
  }

  public void setRemarksType(String remarksType) {
    this.remarksType = remarksType;
  }

  public Remarks remarksDetails(List<RemarksDetail> remarksDetails) {
    this.remarksDetails = remarksDetails;
    return this;
  }

  public Remarks addRemarksDetailsItem(RemarksDetail remarksDetailsItem) {
    this.remarksDetails.add(remarksDetailsItem);
    return this;
  }

   /**
   * Array of Remarks Details
   * @return remarksDetails
  **/
  @ApiModelProperty(required = true, value = "Array of Remarks Details")
  @NotNull

  @Valid
 @Size(min=1)
  public List<RemarksDetail> getRemarksDetails() {
    return remarksDetails;
  }

  public void setRemarksDetails(List<RemarksDetail> remarksDetails) {
    this.remarksDetails = remarksDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Remarks remarks = (Remarks) o;
    return Objects.equals(this.id, remarks.id) &&
        Objects.equals(this.tenantId, remarks.tenantId) &&
        Objects.equals(this.typeOfDocument, remarks.typeOfDocument) &&
        Objects.equals(this.remarksType, remarks.remarksType) &&
        Objects.equals(this.remarksDetails, remarks.remarksDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, typeOfDocument, remarksType, remarksDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Remarks {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    typeOfDocument: ").append(toIndentedString(typeOfDocument)).append("\n");
    sb.append("    remarksType: ").append(toIndentedString(remarksType)).append("\n");
    sb.append("    remarksDetails: ").append(toIndentedString(remarksDetails)).append("\n");
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

