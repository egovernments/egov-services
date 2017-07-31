package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.enums.BusinessNatureEnum;
import org.egov.enums.OwnerShipTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a TradeLicense
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeLicense {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	private ApplicationTypeEnum applicationType = null;

	@Size(min = 6, max = 128)
	private String applicationNumber = null;

	@Size(min = 6, max = 128)
	private String licenseNumber = null;

	@Size(min = 6, max = 128)
	private String oldLicenseNumber = null;

	private String applicationDate = null;

	@Size(max = 12)
	private String adhaarNumber = null;

	@Size(max = 10)
	@NotNull
	private String mobileNumber = null;

	@Size(min = 6, max = 32)
	@NotNull
	private String ownerName = null;

	@Size(min = 6, max = 32)
	@NotNull
	private String fatherSpouseName = null;

	@NotNull
	private String emailId = null;

	@Size(max = 256)
	@NotNull
	private String ownerAddress = null;

	@Size(max = 15)
	private String propertyAssesmentNo = null;

	@NotNull
	private Long localityId = null;

	@NotNull
	private Long wardId = null;

	@Size(max = 256)
	@NotNull
	private String tradeAddress = null;

	@NotNull
	private OwnerShipTypeEnum ownerShipType = null;

	@Size(max = 33)
	@NotNull
	private String tradeTitle = null;

	@NotNull
	private BusinessNatureEnum tradeType = null;

	@NotNull
	private Long categoryId = null;

	@NotNull
	private Long subCategoryId = null;

	@NotNull
	private Long uomId = null;

	@NotNull
	private Double uomValue = null;

	@Size(max = 256)
	private String remarks = null;

	@NotNull
	private String tradeCommencementDate = null;

	private String agrementDate = null;

	@Size(max = 128)
	private String agrementNo = null;

	private Boolean isLegacy = null;

	private Boolean active = null;

	private List<LicenseFeeDetail> feeDetails = null;

	private List<SupportDocument> supportDocuments = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}