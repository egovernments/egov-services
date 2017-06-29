package org.egov.commons.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode

public class BusinessAccountDetails {

	@NotNull
	private Long id;

	@NotNull
	private BusinessDetails businessDetails;

	@NotNull
	private Long chartOfAccount;

	@NotNull
	private Double amount;

	@NotNull
	private String tenantId;

	private List<BusinessAccountSubLedgerDetails> subledgerDetails;

	public BusinessAccountDetails(org.egov.commons.web.contract.BusinessAccountDetails details,
			BusinessDetails modelDetails, boolean isUpdate) {
		id = details.getId();
		chartOfAccount = details.getChartOfAccounts();
		amount = details.getAmount();
		tenantId = modelDetails.getTenantId();
		if (!isUpdate)
			businessDetails = modelDetails;
		else
			businessDetails = getBusinessDetailsForUpdate(details);
	}

	public BusinessAccountDetails toDomainModel() {
		BusinessDetails details = BusinessDetails.builder().id(businessDetails.getId()).build();
		return BusinessAccountDetails.builder().id(id).amount(amount).chartOfAccount(chartOfAccount).tenantId(tenantId)
				.businessDetails(details).build();
	}

	private BusinessDetails getBusinessDetailsForUpdate(org.egov.commons.web.contract.BusinessAccountDetails details) {

		return BusinessDetails.builder().id(details.getBusinessDetails().getId()).build();
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("beginning");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessAccountDetails other = (BusinessAccountDetails) obj;
		if (chartOfAccount == null) {
			if (other.chartOfAccount != null)
				return false;
		} else if (!chartOfAccount.equals(other.chartOfAccount))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		if (businessDetails == null) {
			if (other.businessDetails != null)
				return false;
		} else if (!businessDetails.equals(other.businessDetails))
			return false;

		return true;

	}
}
