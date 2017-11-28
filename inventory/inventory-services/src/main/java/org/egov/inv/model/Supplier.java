package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the Supplier information.
 */
@ApiModel(description = "This object holds the Supplier information. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-06T10:16:53.961Z")

public class Supplier {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	/**
	 * type of the Supplier
	 */
	public enum TypeEnum {
		INDIVIDUAL("INDIVIDUAL"),

		FIRM("FIRM"),

		COMPANY("COMPANY"),

		REGISTEREDSOCIETY("REGISTEREDSOCIETY"),

		GOVERNMENTDEPARTMENT("GOVERNMENTDEPARTMENT"),

		OTHERS("OTHERS");

		private String value;

		TypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static TypeEnum fromValue(String text) {
			for (TypeEnum b : TypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("type")
	private TypeEnum type = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("address")
	private String address = null;

	/**
	 * status of the Supplier
	 */
	public enum StatusEnum {
		ACTIVE("ACTIVE"),

		SUSPENDED("SUSPENDED"),

		BARRED("BARRED"),

		INACTIVE("INACTIVE");

		private String value;

		StatusEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static StatusEnum fromValue(String text) {
			for (StatusEnum b : StatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("status")
	private StatusEnum status = null;

	@JsonProperty("inActiveDate")
	private Long inActiveDate = null;

	@JsonProperty("active")
	private Boolean active;

	@JsonProperty("contactNo")
	private String contactNo = null;

	@JsonProperty("faxNo")
	private String faxNo = null;

	@JsonProperty("website")
	private String website = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("panNo")
	private String panNo = null;

	@JsonProperty("tinNo")
	private String tinNo = null;

	@JsonProperty("cstNo")
	private String cstNo = null;

	@JsonProperty("vatNo")
	private String vatNo = null;

	@JsonProperty("gstNo")
	private String gstNo = null;

	@JsonProperty("contactPerson")
	private String contactPerson = null;

	@JsonProperty("contactPersonNo")
	private String contactPersonNo = null;

	@JsonProperty("bankCode")
	private String bankCode = null;

	@JsonProperty("bankName")
	private String bankName = null;

	@JsonProperty("bankBranch")
	private String bankBranch = null;

	@JsonProperty("acctNo")
	private String acctNo = null;

	@JsonProperty("ifsc")
	private String ifsc = null;

	@JsonProperty("micr")
	private String micr = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public Supplier id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Supplier
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Supplier ")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Supplier active(Boolean active) {
		this.active = active;
		return this;
	}

	@ApiModelProperty(value = "Active Status of the Supplier ")

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Supplier tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Supplier
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(value = "Tenant id of the Supplier")
	@NotNull
	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Supplier type(TypeEnum type) {
		this.type = type;
		return this;
	}

	/**
	 * type of the Supplier
	 * 
	 * @return type
	 **/
	@ApiModelProperty(required = true, value = "type of the Supplier")
	@NotNull

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public Supplier code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * code of the Supplier
	 * 
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "code of the Supplier ")
	@NotNull

	@Pattern(regexp = "^[a-zA-Z0-9]*$")
	@Size(max = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Supplier name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * name of the Material
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "name of the Material ")
	@NotNull

	@Pattern(regexp = "^[a-zA-Z ]*$")
	@Size(max = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Supplier address(String address) {
		this.address = address;
		return this;
	}

	/**
	 * address of the Supplier
	 * 
	 * @return address
	 **/
	@ApiModelProperty(required = true, value = "address of the Supplier   ")
	@NotNull

	@Size(max = 1000)
	@Pattern(regexp = "^[#.0-9a-zA-Z, -]*$")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Supplier status(StatusEnum status) {
		this.status = status;
		return this;
	}

	/**
	 * status of the Supplier
	 * 
	 * @return status
	 **/
	@ApiModelProperty(value = "status of the Supplier")

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Supplier inActiveDate(Long inActiveDate) {
		this.inActiveDate = inActiveDate;
		return this;
	}

	/**
	 * inactive date of the Supplier
	 * 
	 * @return inActiveDate
	 **/
	@ApiModelProperty(value = "inactive date of the Supplier ")

	public Long getInActiveDate() {
		return inActiveDate;
	}

	public void setInActiveDate(Long inActiveDate) {
		this.inActiveDate = inActiveDate;
	}

	public Supplier contactNo(String contactNo) {
		this.contactNo = contactNo;
		return this;
	}

	/**
	 * contact no of the Supplier
	 * 
	 * @return contactNo
	 **/
	@ApiModelProperty(required = true, value = "contact no of the Supplier    ")
	@NotNull

	@Pattern(regexp = "^[0-9]*$")
	@Size(max = 10)
	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Supplier faxNo(String faxNo) {
		this.faxNo = faxNo;
		return this;
	}

	/**
	 * fax number of Supplier
	 * 
	 * @return faxNo
	 **/
	@ApiModelProperty(value = "fax number of Supplier  ")
	@Size(max = 15)
	@Pattern(regexp = "^[0-9]*$")
	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public Supplier website(String website) {
		this.website = website;
		return this;
	}

	/**
	 * website of the Supplier
	 * 
	 * @return website
	 **/
	@ApiModelProperty(value = "website of the Supplier ")
	@Size(max = 500)
	@Pattern(regexp = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Supplier email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * email of the Supplier
	 * 
	 * @return email
	 **/
	@ApiModelProperty(value = "email of the Supplier ")

	@Pattern(regexp = "^$|([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
	@Size(max = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Supplier description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * description of the Supplier
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "description of the Supplier ")

	@Pattern(regexp = "^[,.a-zA-Z 0-9]*$")
	@Size(max = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Supplier panNo(String panNo) {
		this.panNo = panNo;
		return this;
	}

	/**
	 * pan number of supplier
	 * 
	 * @return panNo
	 **/
	@ApiModelProperty(value = "pan number of supplier ")
    @Pattern(regexp ="[A-Z]{5}[0-9]{4}[A-Z]{1}")
	@Size(max = 10,min = 10)
	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public Supplier tinNo(String tinNo) {
		this.tinNo = tinNo;
		return this;
	}

	/**
	 * tin number of supplier
	 * 
	 * @return tinNo
	 **/
	@ApiModelProperty(value = "tin number of supplier    ")

	@Size(max = 11, min = 11)
	@Pattern(regexp = "[0-9]{2}[A-Za-z0-9-!@#$%&*.?=]{9}")
	public String getTinNo() {
		return tinNo;
	}

	public void setTinNo(String tinNo) {
		this.tinNo = tinNo;
	}

	public Supplier cstNo(String cstNo) {
		this.cstNo = cstNo;
		return this;
	}

	/**
	 * cst number of supplier
	 * 
	 * @return cstNo
	 **/
	@ApiModelProperty(value = "cst number of supplier   ")
	@Size(max = 11, min = 11)
	@Pattern(regexp = "[0-9]{2}[A-Za-z0-9-!@#$%&*.?=]{8}[C]{1}")

	public String getCstNo() {
		return cstNo;
	}

	public void setCstNo(String cstNo) {
		this.cstNo = cstNo;
	}

	public Supplier vatNo(String vatNo) {
		this.vatNo = vatNo;
		return this;
	}

	/**
	 * vat number of supplier
	 * 
	 * @return vatNo
	 **/
	@ApiModelProperty(value = "vat number of supplier   ")
	@Size(max = 11, min = 11)
	@Pattern(regexp = "[0-9]{2}[A-Za-z0-9-!@#$%&*.?=]{8}[V]{1}")
	public String getVatNo() {
		return vatNo;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public Supplier gstNo(String gstNo) {
		this.gstNo = gstNo;
		return this;
	}

	/**
	 * gst number of supplier
	 * 
	 * @return gstNo
	 **/
	@ApiModelProperty(value = "gst number of supplier  ")
	@Size(min = 15,max = 15)
	@Pattern(regexp ="[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9A-Za-z]{1}[Z]{1}[0-9]{1}")
	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public Supplier contactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
		return this;
	}

	/**
	 * name of the contact person
	 * 
	 * @return contactPerson
	 **/
	@ApiModelProperty(value = "name of the contact person    ")

	@Pattern(regexp = "^[a-zA-Z ]*$")
	@Size(max = 50)
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Supplier contactPersonNo(String contactPersonNo) {
		this.contactPersonNo = contactPersonNo;
		return this;
	}

	/**
	 * contact number of the contact person
	 * 
	 * @return contactPersonNo
	 **/
	@ApiModelProperty(value = "contact number of the contact person   ")

	@Pattern(regexp = "^[0-9]*$")
	@Size(max = 10)
	public String getContactPersonNo() {
		return contactPersonNo;
	}

	public void setContactPersonNo(String contactPersonNo) {
		this.contactPersonNo = contactPersonNo;
	}

	public Supplier bankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}

	/**
	 * code of the bank
	 * 
	 * @return bankCode
	 **/
	@ApiModelProperty(required = true, value = "code of the bank  ")
	@NotNull
	@Size(max = 50)
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Supplier bankName(String bankName) {
		this.bankName = bankName;
		return this;
	}

	/**
	 * name of the bank
	 * 
	 * @return bankName
	 **/
	@ApiModelProperty(value = "name of the bank ")
	@Size(max = 100)
	@Pattern(regexp = "^[a-zA-Z ]*$")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Supplier bankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
		return this;
	}

	/**
	 * name of the bank branch
	 * 
	 * @return bankBranch
	 **/
	@ApiModelProperty(value = "name of the bank branch ")
	@Size(max = 100)
	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public Supplier acctNo(String acctNo) {
		this.acctNo = acctNo;
		return this;
	}

	/**
	 * account number in the bank
	 * 
	 * @return acctNo
	 **/
	@ApiModelProperty(required = true, value = "account number in the bank  ")
	@NotNull
	@Size(max = 16)
	@Pattern(regexp = "^[0-9]*$")
	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public Supplier ifsc(String ifsc) {
		this.ifsc = ifsc;
		return this;
	}

	/**
	 * ifsc of the bank
	 * 
	 * @return ifsc
	 **/
	@ApiModelProperty(required = true, value = "ifsc of the bank ")
	@NotNull
	@Size(max = 10)
	@Pattern(regexp = "^[a-zA-Z0-9]*$")
	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public Supplier micr(String micr) {
		this.micr = micr;
		return this;
	}

	/**
	 * micr of the bank
	 * 
	 * @return micr
	 **/
	@ApiModelProperty(value = "micr of the bank                ")
	@Size(max = 10)
	@Pattern(regexp = "^[a-zA-Z0-9]*$")
	public String getMicr() {
		return micr;
	}

	public void setMicr(String micr) {
		this.micr = micr;
	}

	public Supplier auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}

	/**
	 * Get auditDetails
	 * 
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
		Supplier supplier = (Supplier) o;
		return Objects.equals(this.id, supplier.id)
				&& Objects.equals(this.tenantId, supplier.tenantId)
				&& Objects.equals(this.type, supplier.type)
				&& Objects.equals(this.code, supplier.code)
				&& Objects.equals(this.name, supplier.name)
				&& Objects.equals(this.address, supplier.address)
				&& Objects.equals(this.status, supplier.status)
				&& Objects.equals(this.inActiveDate, supplier.inActiveDate)
				&& Objects.equals(this.contactNo, supplier.contactNo)
				&& Objects.equals(this.faxNo, supplier.faxNo)
				&& Objects.equals(this.website, supplier.website)
				&& Objects.equals(this.email, supplier.email)
				&& Objects.equals(this.description, supplier.description)
				&& Objects.equals(this.panNo, supplier.panNo)
				&& Objects.equals(this.tinNo, supplier.tinNo)
				&& Objects.equals(this.cstNo, supplier.cstNo)
				&& Objects.equals(this.vatNo, supplier.vatNo)
				&& Objects.equals(this.gstNo, supplier.gstNo)
				&& Objects.equals(this.contactPerson, supplier.contactPerson)
				&& Objects.equals(this.contactPersonNo,
						supplier.contactPersonNo)
				&& Objects.equals(this.bankCode, supplier.bankCode)
				&& Objects.equals(this.bankName, supplier.bankName)
				&& Objects.equals(this.bankBranch, supplier.bankBranch)
				&& Objects.equals(this.acctNo, supplier.acctNo)
				&& Objects.equals(this.ifsc, supplier.ifsc)
				&& Objects.equals(this.micr, supplier.micr)
				&& Objects.equals(this.auditDetails, supplier.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, type, code, name, address, status,
				inActiveDate, contactNo, faxNo, website, email, description,
				panNo, tinNo, cstNo, vatNo, gstNo, contactPerson,
				contactPersonNo, bankCode, bankName, bankBranch, acctNo, ifsc,
				micr, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Supplier {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId))
				.append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address))
				.append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    inActiveDate: ").append(toIndentedString(inActiveDate))
				.append("\n");
		sb.append("    contactNo: ").append(toIndentedString(contactNo))
				.append("\n");
		sb.append("    faxNo: ").append(toIndentedString(faxNo)).append("\n");
		sb.append("    website: ").append(toIndentedString(website))
				.append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    description: ").append(toIndentedString(description))
				.append("\n");
		sb.append("    panNo: ").append(toIndentedString(panNo)).append("\n");
		sb.append("    tinNo: ").append(toIndentedString(tinNo)).append("\n");
		sb.append("    cstNo: ").append(toIndentedString(cstNo)).append("\n");
		sb.append("    vatNo: ").append(toIndentedString(vatNo)).append("\n");
		sb.append("    gstNo: ").append(toIndentedString(gstNo)).append("\n");
		sb.append("    contactPerson: ").append(toIndentedString(contactPerson))
				.append("\n");
		sb.append("    contactPersonNo: ")
				.append(toIndentedString(contactPersonNo)).append("\n");
		sb.append("    bankCode: ").append(toIndentedString(bankCode))
				.append("\n");
		sb.append("    bankName: ").append(toIndentedString(bankName))
				.append("\n");
		sb.append("    bankBranch: ").append(toIndentedString(bankBranch))
				.append("\n");
		sb.append("    acctNo: ").append(toIndentedString(acctNo)).append("\n");
		sb.append("    ifsc: ").append(toIndentedString(ifsc)).append("\n");
		sb.append("    micr: ").append(toIndentedString(micr)).append("\n");
		sb.append("    auditDetails: ").append(toIndentedString(auditDetails))
				.append("\n");
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
