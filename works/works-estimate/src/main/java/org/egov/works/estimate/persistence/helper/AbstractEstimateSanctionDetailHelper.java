package org.egov.works.estimate.persistence.helper;

import org.egov.works.estimate.web.contract.AbstractEstimateSanctionDetail;
import org.egov.works.estimate.web.contract.EstimateSanctionAuthority;
import org.egov.works.estimate.web.contract.EstimateSanctionType;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object that holds the basic data of Technical Sanction for Detailed
 * Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Technical Sanction for Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T07:36:50.343Z")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AbstractEstimateSanctionDetailHelper {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimate")
	private String abstractEstimate = null;

	@JsonProperty("sanctionType")
	private String sanctionType = null;

	@JsonProperty("sanctionAuthority")
	private String sanctionAuthority = null;

	public AbstractEstimateSanctionDetail toDomain() {

		final AbstractEstimateSanctionDetail abstractEstimateSanctionDetail = new AbstractEstimateSanctionDetail();
		abstractEstimateSanctionDetail.setId(this.id);
		abstractEstimateSanctionDetail.setTenantId(this.tenantId);
		abstractEstimateSanctionDetail.setAbstractEstimate(this.abstractEstimate);
		abstractEstimateSanctionDetail.setSanctionType(EstimateSanctionType.valueOf(this.sanctionType));
		abstractEstimateSanctionDetail.setSanctionAuthority(new EstimateSanctionAuthority());
		abstractEstimateSanctionDetail.getSanctionAuthority().setName(this.sanctionAuthority);
		return abstractEstimateSanctionDetail;

	}
}
