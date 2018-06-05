package org.egov.pt.web.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Property
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
//@EqualsAndHashCode(of= {"id","tenantId"})
public class Property extends PropertyInfo{


	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;


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
	private CreationReasonEnum creationReason;

	@JsonProperty("occupancyDate")
	private Long occupancyDate;

	@JsonProperty("propertyDetails")
	private List<PropertyDetail> propertyDetails;


	public Property addpropertyDetailsItem(PropertyDetail propertyDetailsItem) {
		if (this.propertyDetails == null) {
			this.propertyDetails = new ArrayList<>();
		}
		this.propertyDetails.add(propertyDetailsItem);
		return this;
	}


	public static PropertyBuilder builder(){
		return new PropertyBuilder();
	}

    public static class PropertyBuilder{

		private CreationReasonEnum creationReason;
		private Long occupancyDate;
		private List<PropertyDetail>  propertyDetails;
		private AuditDetails auditDetails;

		private String propertyId;
		private String tenantId;
		private String acknowldgementNumber;
		private String oldPropertyId;
		private StatusEnum status;
		private Address address;



		public PropertyBuilder creationReason(CreationReasonEnum creationReason){
			this.creationReason=creationReason;
			return this;
		}

		public PropertyBuilder occupancyDate(Long occupancyDate){
			this.occupancyDate=occupancyDate;
			return this;
		}

		public PropertyBuilder propertyDetail( List<PropertyDetail> propertyDetails){
			this.propertyDetails=propertyDetails;
			return this;
		}

		public PropertyBuilder auditDetails(AuditDetails auditDetails){
			this.auditDetails=auditDetails;
			return this;
		}

		public PropertyBuilder propertyId(String propertyId){
			this.propertyId =propertyId ;
			return this;
		}

		public PropertyBuilder tenantId(String tenantId){
			this.tenantId =tenantId ;
			return this;
		}

		public PropertyBuilder acknowldgementNumber(String acknowldgementNumber){
			this.acknowldgementNumber =acknowldgementNumber ;
			return this;
		}

		public PropertyBuilder oldPropertyId(String oldPropertyId){
			this.oldPropertyId=oldPropertyId;
			return this;
		}

		public PropertyBuilder status(StatusEnum status){
			this.status = status;
			return this;
		}

		public PropertyBuilder address(Address address){
			this.address = address;
			return this;
		}

		public Property build(){
			return new Property(this);
		}


	}


	public Property(PropertyBuilder builder) {
		super(builder.propertyId,builder.tenantId,builder.acknowldgementNumber,builder.oldPropertyId,builder.status,builder.address);
		this.auditDetails = builder.auditDetails;
		this.creationReason = builder.creationReason;
		this.occupancyDate = builder.occupancyDate;
		this.propertyDetails = builder.propertyDetails;

	}
}




/*	@JsonProperty("id")
	private String id;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("acknowldgementNumber")
	private String acknowldgementNumber;

	@JsonProperty("assessmentNumber")
	private String assessmentNumber;*/

	/**
	 * status of the Property
	 *//*
	public enum StatusEnum {
		ACTIVE("ACTIVE"),

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
	private StatusEnum status;

	@JsonProperty("financialYear")
	private String financialYear;

	@JsonProperty("propertyType")
	private String propertyType;

	@JsonProperty("propertySubType")
	private String propertySubType;

	@JsonProperty("usageCategoryMajor")
	private String usageCategoryMajor;

	@JsonProperty("owners")
	@Valid
	@NotNull
	@Size(min=1)
	private Set<OwnerInfo> owners;

	@JsonProperty("address")
	private Address address;*/

	/*@JsonProperty("oldAssessmentNumber")
	private String oldAssessmentNumber;

	@JsonProperty("assessmentDate")
	private Long assessmentDate;*/

/**
 * New property comes into system either property is newly constructed or
 * existing property got sub divided. Here the reason for creation will be
 * captured.
 */

/*
	public Property addOwnersItem(OwnerInfo ownersItem) {
		if (this.owners == null) {
			this.owners = new HashSet<>();
		}
		this.owners.add(ownersItem);
		return this;
	}*/
