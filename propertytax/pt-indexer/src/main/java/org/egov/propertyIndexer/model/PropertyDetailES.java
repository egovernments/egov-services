package org.egov.propertyIndexer.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.SourceEnum;
import org.egov.enums.StatusEnum;
import org.egov.models.AssessmentDate;
import org.egov.models.AuditDetails;
import org.egov.models.BuilderDetail;
import org.egov.models.Document;
import org.egov.models.Factors;
import org.egov.models.WorkFlowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 *This class will be used for Elasticseach indexing only
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertyDetailES {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("source")
	private SourceEnum source = null;

	@JsonProperty("regdDocNo")
	@Size(min = 1, max = 64)
	private String regdDocNo = null;

	@JsonProperty("regdDocDate")
	private String regdDocDate = null;

	@JsonProperty("reason")
	@Size(min = 1, max = 64)
	private String reason = null;

	@JsonProperty("status")
	private StatusEnum status = null;

	@JsonProperty("isVerified")
	private Boolean isVerified = null;

	@JsonProperty("verificationDate")
	private String verificationDate = null;

	@JsonProperty("isExempted")
	private Boolean isExempted = false;

	@JsonProperty("exemptionReason")
	@Size(min = 1, max = 32)
	private String exemptionReason = null;

	@JsonProperty("propertyType")
	@NotNull
	@Size(min = 1, max = 64)
	private PropertyTypeES propertyType = null;

	@JsonProperty("category")
	@Size(min = 1, max = 64)
	private String category = null;

	@JsonProperty("usage")
	@Size(min = 1, max = 64)
	private Usage usage = null;

	@JsonProperty("department")
	@Size(min = 1, max = 64)
	private DepartmentES department = null;

	@JsonProperty("apartment")
	@Size(min = 1, max = 64)
	private ApartmentES apartment = null;

	@JsonProperty("siteLength")
	private Double siteLength = null;

	@JsonProperty("siteBreadth")
	private Double siteBreadth = null;

	@JsonProperty("sitalArea")
	private Double sitalArea = null;

	@JsonProperty("totalBuiltupArea")
	private Double totalBuiltupArea = null;

	@JsonProperty("undividedShare")
	private Double undividedShare = null;

	@JsonProperty("noOfFloors")
	private Long noOfFloors = null;

	@JsonProperty("isSuperStructure")
	private Boolean isSuperStructure = false;

	@JsonProperty("landOwner")
	@Size(min = 1, max = 128)
	private String landOwner = null;

	@JsonProperty("floorType")
	@Size(min = 1, max = 64)
	private FloorTypeES floorType = null;

	@JsonProperty("woodType")
	@Size(min = 1, max = 64)
	private WoodTypeES woodType = null;

	@JsonProperty("roofType")
	@Size(min = 1, max = 64)
	private RoofTypeES roofType = null;

	@JsonProperty("wallType")
	@Size(min = 1, max = 64)
	private WallTypeES wallType = null;

	@JsonProperty("floors")
	@Valid
	private List<FloorES> floors = new ArrayList<FloorES>();

	@JsonProperty("documents")
	private List<Document> documents = new ArrayList<Document>();

	@JsonProperty("stateId")
	private String stateId = null;

	@JsonProperty("applicationNo")
	@Size(min = 1, max = 64)
	private String applicationNo = null;

	@JsonProperty("taxCalculations")
	private String taxCalculations = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("factors")
	private List<Factors> factors;

	@JsonProperty("assessmentDates")
	private List<AssessmentDate> assessmentDates = null;

	@JsonProperty("builderDetails")
	private BuilderDetail builderDetails = null;
	
	@Size(min = 1, max = 16)
    @JsonProperty("bpaNo")
    private String bpaNo = null;
    
    @JsonProperty("bpaDate")
    private String bpaDate;
    
    @JsonProperty("subUsage")
    @Size(min = 1, max = 128)
    private SubUsage subUsage = null;
}
