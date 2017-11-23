package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Unit of Measurement 
 */
@ApiModel(description = "Unit of Measurement ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-20T09:33:12.146Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Uom   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("code")
  private String code = null;
  
  @JsonProperty("description")
  private String description = null;
  
  @JsonProperty("baseUom")
  private Boolean baseUom = false;
  
  @JsonProperty("uomCategory")
  private String uomCategory = null;
  
  @JsonProperty("fromDate")
  private Long fromDate;
    
  @JsonProperty("toDate")
  private Long toDate;
  
  @JsonProperty("conversionFactor")
  private BigDecimal conversionFactor = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Uom id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the uom 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the uom ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Uom tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the uom
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the uom")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Uom name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the uom 
   * @return name
  **/
  @ApiModelProperty(value = "name of the uom ")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Uom code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the uom 
   * @return code
  **/
  @ApiModelProperty(value = "code of the uom ")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
  
  public Uom description(String description) {
	    this.description = description;
	    return this;
	  }
  
  public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	  public Uom baseUom(Boolean baseUom) {
		    this.baseUom = baseUom;
		    return this;
		  }

	public Boolean getBaseUom() {
		return baseUom;
	}

	public void setBaseUom(Boolean baseuom) {
		this.baseUom = baseUom;
	}

	  public Uom uomCategory(String uomCategory) {
		    this.uomCategory = uomCategory;
		    return this;
		  }
	
	public String getUomCategory() {
		return uomCategory;
	}

	public void setUomCategory(String uomCategory) {
		this.uomCategory = uomCategory;
	}

	  public Uom fromDate(Long fromDate) {
		    this.fromDate = fromDate;
		    return this;
		  }
	
	public Long getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}
	
	  public Uom toDate(Long toDate) {
		    this.toDate = toDate;
		    return this;
		  }

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

  public Uom conversionFactor(BigDecimal conversionFactor) {
    this.conversionFactor = conversionFactor;
    return this;
  }

   /**
   * conversionFactor of the particular uom 
   * @return conversionFactor
  **/
  @ApiModelProperty(value = "conversionFactor of the particular uom ")

  @Valid

  public BigDecimal getConversionFactor() {
    return conversionFactor;
  }

  public void setConversionFactor(BigDecimal conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  public Uom active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Boolean flag of the uom 
   * @return active
  **/
  @ApiModelProperty(value = "Boolean flag of the uom ")


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Uom auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uom uom = (Uom) o;
    return Objects.equals(this.id, uom.id) &&
        Objects.equals(this.tenantId, uom.tenantId) &&
        Objects.equals(this.name, uom.name) &&
        Objects.equals(this.code, uom.code) &&
        Objects.equals(this.description, uom.description) &&
        Objects.equals(this.uomCategory, uom.uomCategory) &&
        Objects.equals(this.baseUom, uom.baseUom) &&
        Objects.equals(this.fromDate, uom.fromDate) &&
        Objects.equals(this.toDate, uom.toDate) &&
        Objects.equals(this.conversionFactor, uom.conversionFactor) &&
        Objects.equals(this.active, uom.active) &&
        Objects.equals(this.auditDetails, uom.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, code,description, uomCategory, baseUom, fromDate, toDate, conversionFactor, active, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Uom {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    uomCategory: ").append(toIndentedString(uomCategory)).append("\n");
    sb.append("    baseUom: ").append(toIndentedString(baseUom)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("    conversionFactor: ").append(toIndentedString(conversionFactor)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

