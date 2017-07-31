package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.enums.BusinessNatureEnum;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in FeeMatrix
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeMatrix {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	private ApplicationTypeEnum applicationType = null;

	@NotNull
	private Long categoryId = null;

	@NotNull
	private BusinessNatureEnum businessNature = null;

	@NotNull
	private Long subCategoryId = null;

	@NotNull
	@DateTimeFormat(pattern = "YYYY-YY")
	private String financialYear = null;

	private String effectiveFrom = null;

	private String effectiveTo = null;

	private List<FeeMatrixDetail> feeMatrixDetails;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}