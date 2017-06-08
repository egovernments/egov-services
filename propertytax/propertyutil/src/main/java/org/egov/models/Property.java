package org.egov.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A Object holds the basic data for a property
 */

public class Property   {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size( min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@NotNull
	@Size(min = 6, max = 128)
	private String upicNumber = null;

	@JsonProperty("oldUpicNumber")
	@Size(min = 4, max = 128)
	private String oldUpicNumber = null;

	@JsonProperty("vltUpicNumber")
	@Size(min = 4, max = 128)
	private String vltUpicNumber = null;

	/**
	 * New property comes into system either property is newly constructed or existing property got sub divided. Here the reason for creation will be captured.
	 */
	public enum CreationReasonEnum {
		NEWPROPERTY("NEWPROPERTY"),

		SUBDIVISION("SUBDIVISION");

		private String value;

		CreationReasonEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static CreationReasonEnum fromValue(String text) {
			for (CreationReasonEnum b : CreationReasonEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("creationReason")
	@Size(min = 1, max = 256)
	private CreationReasonEnum creationReason = null;

	@JsonProperty("address")
	@NotNull
	private Address address = null;

	@JsonProperty("owners")
	@Valid
	@NotNull
	private List<OwnerInfo> owners = new ArrayList<OwnerInfo>();

	@JsonProperty("propertyDetail")
	@Valid
	@NotNull
	private PropertyDetail propertyDetail = null;

	@JsonProperty("vacantLand")
	@Valid
	private VacantLandDetail vacantLand = null;

	@JsonProperty("assessmentDate")
	private String assessmentDate = null;

	@JsonProperty("occupancyDate")
	@NotNull
	private String occupancyDate = null;

	@JsonProperty("gisRefNo")
	@Size(min=4,max=32)
	private String gisRefNo = null;

	@JsonProperty("isAuthorised")
	private Boolean isAuthorised = true;

	@JsonProperty("isUnderWorkflow")
	private Boolean isUnderWorkflow = false;

	@JsonProperty("boundary")
	@NotNull
	private PropertyLocation boundary = null;

	@JsonProperty("active")
	private Boolean active = true;

	/**
	 * Property can be created from different channels Eg. System (properties created by ULB officials), CFC Counter (From citizen faciliation counters) etc. Here we are defining some known channels, there can be more client to client.
	 */
	public enum ChannelEnum {
		SYSTEM("SYSTEM"),

		CFC_COUNTER("CFC_COUNTER"),

		CITIZEN("CITIZEN");

		private String value;

		ChannelEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static ChannelEnum fromValue(String text) {
			for (ChannelEnum b : ChannelEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("channel")
	@NotNull
	@Size(min=4,max=16)
	private ChannelEnum channel = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public Property id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Property
	 * @return id
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Property tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * tenant id of the Property
	 * @return tenantId
	 **/
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Property upicNumber(String upicNumber) {
		this.upicNumber = upicNumber;
		return this;
	}

	/**
	 * UPIC (Unique Property Identification Code) of the Property. This is a unique in system. This is mandatory but always be generated on the final approval.
	 * @return upicNumber
	 **/
	public String getUpicNumber() {
		return upicNumber;
	}

	public void setUpicNumber(String upicNumber) {
		this.upicNumber = upicNumber;
	}

	public Property oldUpicNumber(String oldUpicNumber) {
		this.oldUpicNumber = oldUpicNumber;
		return this;
	}

	/**
	 * Old upic no of the Property. ULBs have the existing property in their system/manual records with their identification number, they want to continue the old reference number in this case the same identification number will be captured here.
	 * @return oldUpicNumber
	 **/
	public String getOldUpicNumber() {
		return oldUpicNumber;
	}

	public void setOldUpicNumber(String oldUpicNumber) {
		this.oldUpicNumber = oldUpicNumber;
	}

	public Property vltUpicNumber(String vltUpicNumber) {
		this.vltUpicNumber = vltUpicNumber;
		return this;
	}

	/**
	 * Vacant lands also be assessed in system. If building constructed in the same vacant land. The Vacant land upicno will be captured here for a the new building.
	 * @return vltUpicNumber
	 **/

	public String getVltUpicNumber() {
		return vltUpicNumber;
	}

	public void setVltUpicNumber(String vltUpicNumber) {
		this.vltUpicNumber = vltUpicNumber;
	}

	public Property creationReason(CreationReasonEnum creationReason) {
		this.creationReason = creationReason;
		return this;
	}

	/**
	 * New property comes into system either property is newly constructed or existing property got sub divided. Here the reason for creation will be captured.
	 * @return creationReason
	 **/

	public CreationReasonEnum getCreationReason() {
		return creationReason;
	}

	public void setCreationReason(CreationReasonEnum creationReason) {
		this.creationReason = creationReason;
	}

	public Property address(Address address) {
		this.address = address;
		return this;
	}

	/**
	 * Get address
	 * @return address
	 **/
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Property owners(List<OwnerInfo> owners) {
		this.owners = owners;
		return this;
	}

	public Property addOwnersItem(OwnerInfo ownersItem) {
		this.owners.add(ownersItem);
		return this;
	}

	/**
	 * Property owners, these will be citizen users in system.
	 * @return owners
	 **/
	public List<OwnerInfo> getOwners() {
		return owners;
	}

	public void setOwners(List<OwnerInfo> owners) {
		this.owners = owners;
	}

	public Property propertyDetail(PropertyDetail propertyDetail) {
		this.propertyDetail = propertyDetail;
		return this;
	}

	/**
	 * Get propertyDetail
	 * @return propertyDetail
	 **/
	public PropertyDetail getPropertyDetail() {
		return propertyDetail;
	}

	public void setPropertyDetail(PropertyDetail propertyDetail) {
		this.propertyDetail = propertyDetail;
	}

	public Property vacantLand(VacantLandDetail vacantLand) {
		this.vacantLand = vacantLand;
		return this;
	}

	/**
	 * Get vacantLand
	 * @return vacantLand
	 **/
	public VacantLandDetail getVacantLand() {
		return vacantLand;
	}

	public void setVacantLand(VacantLandDetail vacantLand) {
		this.vacantLand = vacantLand;
	}

	public Property assessmentDate(String assessmentDate) {
		this.assessmentDate = assessmentDate;
		return this;
	}

	/**
	 * There will be work flow to create a new property in a system. The assessment date is the final approval and UPIC no generation date.
	 * @return assessmentDate
	 **/
	public String getAssessmentDate() {
		return assessmentDate;
	}

	public void setAssessmentDate(String assessmentDate) {
		this.assessmentDate = assessmentDate;
	}

	public Property occupancyDate(String occupancyDate) {
		this.occupancyDate = occupancyDate;
		return this;
	}

	/**
	 * Property is constructed but the tax will be applicable only from the occupation date. System capture that date here.
	 * @return occupancyDate
	 **/
	public String getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(String occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public Property gisRefNo(String gisRefNo) {
		this.gisRefNo = gisRefNo;
		return this;
	}

	/**
	 * Property tax system will be integrated with GIS system. Here we capture the GIS ref. no.
	 * @return gisRefNo
	 **/
	public String getGisRefNo() {
		return gisRefNo;
	}

	public void setGisRefNo(String gisRefNo) {
		this.gisRefNo = gisRefNo;
	}

	public Property isAuthorised(Boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
		return this;
	}

	/**
	 * Property can be authorised or not authorised in a ULB depends on different parameters Eg. If there is Building plan approval property is a authorised property otherwise its a not a authorised property. System will capture the info here.
	 * @return isAuthorised
	 **/

	public Boolean getIsAuthorised() {
		return isAuthorised;
	}

	public void setIsAuthorised(Boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
	}

	public Property isUnderWorkflow(Boolean isUnderWorkflow) {
		this.isUnderWorkflow = isUnderWorkflow;
		return this;
	}

	/**
	 * There are different transactions can be performed on a property and all the transactions goes through the work flow. If one workflow is happening system should not allow other work flow on a same property so, when any work flow starts we flag this field as true and when the same work flow ends we flag this field as false.
	 * @return isUnderWorkflow
	 **/

	public Boolean getIsUnderWorkflow() {
		return isUnderWorkflow;
	}

	public void setIsUnderWorkflow(Boolean isUnderWorkflow) {
		this.isUnderWorkflow = isUnderWorkflow;
	}

	public Property boundary(PropertyLocation boundary) {
		this.boundary = boundary;
		return this;
	}

	/**
	 * Get boundary
	 * @return boundary
	 **/
	public PropertyLocation getBoundary() {
		return boundary;
	}

	public void setBoundary(PropertyLocation boundary) {
		this.boundary = boundary;
	}

	public Property active(Boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * True if the property is active and False if the property is inactive.
	 * @return active
	 **/
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Property channel(ChannelEnum channel) {
		this.channel = channel;
		return this;
	}

	/**
	 * Property can be created from different channels Eg. System (properties created by ULB officials), CFC Counter (From citizen faciliation counters) etc. Here we are defining some known channels, there can be more client to client.
	 * @return channel
	 **/

	
	public ChannelEnum getChannel() {
		return channel;
	}

	public void setChannel(ChannelEnum channel) {
		this.channel = channel;
	}

	public Property auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}

	/**
	 * Get auditDetails
	 * @return auditDetails
	 **/

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
		Property property = (Property) o;
		return Objects.equals(this.id, property.id) &&
				Objects.equals(this.tenantId, property.tenantId) &&
				Objects.equals(this.upicNumber, property.upicNumber) &&
				Objects.equals(this.oldUpicNumber, property.oldUpicNumber) &&
				Objects.equals(this.vltUpicNumber, property.vltUpicNumber) &&
				Objects.equals(this.creationReason, property.creationReason) &&
				Objects.equals(this.address, property.address) &&
				Objects.equals(this.owners, property.owners) &&
				Objects.equals(this.propertyDetail, property.propertyDetail) &&
				Objects.equals(this.vacantLand, property.vacantLand) &&
				Objects.equals(this.assessmentDate, property.assessmentDate) &&
				Objects.equals(this.occupancyDate, property.occupancyDate) &&
				Objects.equals(this.gisRefNo, property.gisRefNo) &&
				Objects.equals(this.isAuthorised, property.isAuthorised) &&
				Objects.equals(this.isUnderWorkflow, property.isUnderWorkflow) &&
				Objects.equals(this.boundary, property.boundary) &&
				Objects.equals(this.active, property.active) &&
				Objects.equals(this.channel, property.channel) &&
				Objects.equals(this.auditDetails, property.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, upicNumber, oldUpicNumber, vltUpicNumber, creationReason, address, owners, propertyDetail, vacantLand, assessmentDate, occupancyDate, gisRefNo, isAuthorised, isUnderWorkflow, boundary, active, channel, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Property {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    upicNumber: ").append(toIndentedString(upicNumber)).append("\n");
		sb.append("    oldUpicNumber: ").append(toIndentedString(oldUpicNumber)).append("\n");
		sb.append("    vltUpicNumber: ").append(toIndentedString(vltUpicNumber)).append("\n");
		sb.append("    creationReason: ").append(toIndentedString(creationReason)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
		sb.append("    propertyDetail: ").append(toIndentedString(propertyDetail)).append("\n");
		sb.append("    vacantLand: ").append(toIndentedString(vacantLand)).append("\n");
		sb.append("    assessmentDate: ").append(toIndentedString(assessmentDate)).append("\n");
		sb.append("    occupancyDate: ").append(toIndentedString(occupancyDate)).append("\n");
		sb.append("    gisRefNo: ").append(toIndentedString(gisRefNo)).append("\n");
		sb.append("    isAuthorised: ").append(toIndentedString(isAuthorised)).append("\n");
		sb.append("    isUnderWorkflow: ").append(toIndentedString(isUnderWorkflow)).append("\n");
		sb.append("    boundary: ").append(toIndentedString(boundary)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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

