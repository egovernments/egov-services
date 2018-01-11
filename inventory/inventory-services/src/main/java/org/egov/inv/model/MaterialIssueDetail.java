package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object holds the materail issue detail information for both indent and
 * non indent.
 */
@ApiModel(description = "This object holds the materail issue detail information for both indent and non indent. ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-12-27T10:36:36.253Z")

public class MaterialIssueDetail {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("material")
	private Material material = null;

	@JsonProperty("orderNumber")
	private BigDecimal orderNumber = null;

	@JsonProperty("userQuantityIssued")
	private BigDecimal userQuantityIssued = null;

	@JsonProperty("quantityIssued")
	private BigDecimal quantityIssued = null;
	
	@JsonProperty("balanceQuantity")
	private BigDecimal balanceQuantity = null;

	@JsonProperty("value")
	private BigDecimal value = null;

	@JsonProperty("uom")
	private Uom uom = null;

	@JsonProperty("voucherHeader")
	private String voucherHeader = null;

	@JsonProperty("indentDetail")
	private IndentDetail indentDetail = null;

	@JsonProperty("quantityToBeIssued")
	private BigDecimal quantityToBeIssued = null;

	@JsonProperty("pendingIndentQuantity")
	private BigDecimal pendingIndentQuantity = null;

	@JsonProperty("scrapValue")
	private BigDecimal scrapValue = null;

	@JsonProperty("mrnNumber")
	private String mrnNumber = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("scrapedQuantity")
	private BigDecimal scrapedQuantity = null;

	@JsonProperty("materialIssuedFromReceipts")
	private List<MaterialIssuedFromReceipt> materialIssuedFromReceipts = null;

	public MaterialIssueDetail id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Material Issue Details
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Material Issue Details ")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MaterialIssueDetail tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Material Issue Details
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Material Issue Details")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public MaterialIssueDetail material(Material material) {
		this.material = material;
		return this;
	}

	/**
	 * Applicable for Non Indent Issue.
	 * 
	 * @return material
	 **/
	@ApiModelProperty(required = true, value = "Applicable for Non Indent Issue. ")


	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public MaterialIssueDetail orderNumber(BigDecimal orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

	/**
	 * Order of items issued.
	 * 
	 * @return orderNumber
	 **/
	@ApiModelProperty(value = "Order of items issued.")

	public BigDecimal getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(BigDecimal orderNumber) {
		this.orderNumber = orderNumber;
	}

	public MaterialIssueDetail userQuantityIssued(BigDecimal userQuantityIssued) {
		this.userQuantityIssued = userQuantityIssued;
		return this;
	}

	public MaterialIssueDetail pendingIndentQuantity(BigDecimal pendingIndentQuantity) {
		this.pendingIndentQuantity = pendingIndentQuantity;
		return this;
	}

	/**
	 * indentquantity pending of indent.
	 * 
	 * @return pendingIndentQuantity
	 **/
	@ApiModelProperty(value = "Pending Indent Quantity. ")
	

	public BigDecimal getPendingIndentQuantity() {
		return pendingIndentQuantity;
	}

	public void setPendingIndentQuantity(BigDecimal pendingIndentQuantity) {
		this.pendingIndentQuantity = pendingIndentQuantity;
	}

	/**
	 * Quantity issued of the Material Issue Detail.
	 * 
	 * @return userQuantityIssued
	 **/
	@ApiModelProperty(value = "Quantity issued of the Material Issue Detail. ")

	@Valid

	public BigDecimal getUserQuantityIssued() {
		return userQuantityIssued;
	}

	public void setUserQuantityIssued(BigDecimal userQuantityIssued) {
		this.userQuantityIssued = userQuantityIssued;
	}

	public MaterialIssueDetail quantityIssued(BigDecimal quantityIssued) {
		this.quantityIssued = quantityIssued;
		return this;
	}

	/**
	 * Quantity issued of the Material Issue Detail in Base UOM
	 * 
	 * @return quantityIssued
	 **/
	@ApiModelProperty(required = true, value = "Quantity issued of the Material Issue Detail in Base UOM ")


	public BigDecimal getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(BigDecimal quantityIssued) {
		this.quantityIssued = quantityIssued;
	}
	
	public MaterialIssueDetail balanceQuantity(BigDecimal balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
		return this;
	}

	/**
	 * Quantity issued of the Material Issue Detail in Base UOM
	 * 
	 * @return balanceQuantity
	 **/
	@ApiModelProperty(required = true, value = "Quantity issued of the Material Issue Detail in Base UOM ")


	public BigDecimal getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(BigDecimal balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}


	public MaterialIssueDetail value(BigDecimal value) {
		this.value = value;
		return this;
	}

	/**
	 * Total value of issued material. Send value 0 if price not defined.
	 * 
	 * @return value
	 **/
	@ApiModelProperty(required = true, value = "Total value of issued material.   Send value 0 if price not defined. ")


	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public MaterialIssueDetail uom(Uom uom) {
		this.uom = uom;
		return this;
	}

	/**
	 * unit of measure of selected material.
	 * 
	 * @return uom
	 **/
	@ApiModelProperty(required = true, value = "unit of measure of selected material.")
	

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

	public MaterialIssueDetail voucherHeader(String voucherHeader) {
		this.voucherHeader = voucherHeader;
		return this;
	}

	/**
	 * financial voucher passed id map
	 * 
	 * @return voucherHeader
	 **/
	@ApiModelProperty(value = "financial voucher passed id map")

	public String getVoucherHeader() {
		return voucherHeader;
	}

	public void setVoucherHeader(String voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	public MaterialIssueDetail indentDetail(IndentDetail indentDetail) {
		this.indentDetail = indentDetail;
		return this;
	}

	/**
	 * Applicable for Indent Issue. Balance quantity to be issued of the
	 * IndentDetails. Mandatory in case of indent issue.
	 * 
	 * @return indentDetail
	 **/
	@ApiModelProperty(value = "Applicable for Indent Issue. Balance quantity to be issued of the IndentDetails. Mandatory in case of indent issue.")

	public IndentDetail getIndentDetail() {
		return indentDetail;
	}

	public void setIndentDetail(IndentDetail indentDetail) {
		this.indentDetail = indentDetail;
	}

	public MaterialIssueDetail quantityToBeIssued(BigDecimal quantityToBeIssued) {
		this.quantityToBeIssued = quantityToBeIssued;
		return this;
	}

	/**
	 * Balance quantity to be issued of the IndentDetails.
	 * 
	 * @return quantityToBeIssued
	 **/
	@ApiModelProperty(value = "Balance quantity to be issued of the IndentDetails. ")

	public BigDecimal getQuantityToBeIssued() {
		return quantityToBeIssued;
	}

	public void setQuantityToBeIssued(BigDecimal quantityToBeIssued) {
		this.quantityToBeIssued = quantityToBeIssued;
	}

	public MaterialIssueDetail scrapedQuantity(BigDecimal scrapedQuantity) {
		this.scrapedQuantity = scrapedQuantity;
		return this;
	}

	/**
	 * Quantity utilized in scrap creation
	 * 
	 * @return scrapedQuantity
	 **/
	@ApiModelProperty(value = "Quantity utilized in scrap creation ")

	@Valid

	public BigDecimal getScrapedQuantity() {
		return scrapedQuantity;
	}

	public void setScrapedQuantity(BigDecimal scrapedQuantity) {
		this.scrapedQuantity = scrapedQuantity;
	}

	public MaterialIssueDetail scrapValue(BigDecimal scrapValue) {
		this.scrapValue = scrapValue;
		return this;
	}

	/**
	 * If item writeoffed or scrapped, then define the scrap value.
	 * 
	 * @return scrapValue
	 **/
	@ApiModelProperty(value = "If item writeoffed or scrapped, then define the scrap value. ")

	public BigDecimal getScrapValue() {
		return scrapValue;
	}

	public void setScrapValue(BigDecimal scrapValue) {
		this.scrapValue = scrapValue;
	}

	public MaterialIssueDetail mrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
		return this;
	}

	/**
	 * Receipt number reference.
	 * 
	 * @return mrnNumber
	 **/
	@ApiModelProperty(value = "Receipt number reference. ")

	@Size(max = 100)
	public String getMrnNumber() {
		return mrnNumber;
	}

	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	public MaterialIssueDetail description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * description of the Material Issue Detail.
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "description of the Material Issue Detail. ")

	@Size(max = 512)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MaterialIssueDetail materialIssuedFromReceipts(List<MaterialIssuedFromReceipt> materialIssuedFromReceipts) {
		this.materialIssuedFromReceipts = materialIssuedFromReceipts;
		return this;
	}

	public MaterialIssueDetail addMaterialIssuedFromReceiptsItem(
			MaterialIssuedFromReceipt materialIssuedFromReceiptsItem) {
		if (this.materialIssuedFromReceipts == null) {
			this.materialIssuedFromReceipts = new ArrayList<MaterialIssuedFromReceipt>();
		}
		this.materialIssuedFromReceipts.add(materialIssuedFromReceiptsItem);
		return this;
	}

	/**
	 * List of materials issued from receipt detail
	 * 
	 * @return materialIssuedFromReceipts
	 **/
	@ApiModelProperty(value = "List of materials issued from receipt detail")

	public List<MaterialIssuedFromReceipt> getMaterialIssuedFromReceipts() {
		return materialIssuedFromReceipts;
	}

	public void setMaterialIssuedFromReceipts(List<MaterialIssuedFromReceipt> materialIssuedFromReceipts) {
		this.materialIssuedFromReceipts = materialIssuedFromReceipts;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MaterialIssueDetail materialIssueDetail = (MaterialIssueDetail) o;
		return Objects.equals(this.id, materialIssueDetail.id)
				&& Objects.equals(this.tenantId, materialIssueDetail.tenantId)
				&& Objects.equals(this.material, materialIssueDetail.material)
				&& Objects.equals(this.orderNumber, materialIssueDetail.orderNumber)
				&& Objects.equals(this.userQuantityIssued, materialIssueDetail.userQuantityIssued)
				&& Objects.equals(this.quantityIssued, materialIssueDetail.quantityIssued)
				&& Objects.equals(this.balanceQuantity, materialIssueDetail.balanceQuantity)
				&& Objects.equals(this.value, materialIssueDetail.value)
				&& Objects.equals(this.uom, materialIssueDetail.uom)
				&& Objects.equals(this.voucherHeader, materialIssueDetail.voucherHeader)
				&& Objects.equals(this.indentDetail, materialIssueDetail.indentDetail)
				&& Objects.equals(this.quantityToBeIssued, materialIssueDetail.quantityToBeIssued)
				&& Objects.equals(this.scrapedQuantity, materialIssueDetail.scrapedQuantity)
				&& Objects.equals(this.scrapValue, materialIssueDetail.scrapValue)
				&& Objects.equals(this.mrnNumber, materialIssueDetail.mrnNumber)
				&& Objects.equals(this.description, materialIssueDetail.description)
				&& Objects.equals(this.materialIssuedFromReceipts, materialIssueDetail.materialIssuedFromReceipts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, material, orderNumber, userQuantityIssued, quantityIssued, balanceQuantity,
				value, uom, voucherHeader, indentDetail, quantityToBeIssued, scrapedQuantity, scrapValue, mrnNumber,
				description, materialIssuedFromReceipts);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MaterialIssueDetail {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    material: ").append(toIndentedString(material)).append("\n");
		sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
		sb.append("    userQuantityIssued: ").append(toIndentedString(userQuantityIssued)).append("\n");
		sb.append("    quantityIssued: ").append(toIndentedString(quantityIssued)).append("\n");
		sb.append("    balanceQuantity: ").append(toIndentedString(balanceQuantity)).append("\n");
		sb.append("    value: ").append(toIndentedString(value)).append("\n");
		sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
		sb.append("    voucherHeader: ").append(toIndentedString(voucherHeader)).append("\n");
		sb.append("    indentDetail: ").append(toIndentedString(indentDetail)).append("\n");
		sb.append("    quantityToBeIssued: ").append(toIndentedString(quantityToBeIssued)).append("\n");
		sb.append("    scrapedQuantity: ").append(toIndentedString(scrapedQuantity)).append("\n");
		sb.append("    scrapValue: ").append(toIndentedString(scrapValue)).append("\n");
		sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    materialIssuedFromReceipts: ").append(toIndentedString(materialIssuedFromReceipts)).append("\n");
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
