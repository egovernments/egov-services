package org.egov.commons.model;

import javax.validation.constraints.NotNull;

import org.egov.commons.web.contract.BusinessAccountSubLedger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BusinessAccountSubLedgerDetails {

	@NotNull
	private Long id;

	@NotNull
	private Double amount;

	@NotNull
	private BusinessAccountDetails businessAccountDetail;

	private Long accountDetailKey;

	@NotNull
	private Long accountDetailType;

	@NotNull
	private String tenantId;

	public BusinessAccountSubLedgerDetails(BusinessAccountSubLedger subledger, BusinessDetails modelDetails,
			boolean isUpdate) {
		id = subledger.getId();
		amount = subledger.getAmount();
		accountDetailKey = subledger.getDetailKey();
		accountDetailType = subledger.getDetailType();
		tenantId = modelDetails.getTenantId();
		if (!isUpdate)
			businessAccountDetail = getModelAccountDetail(subledger.getBusinessAccountDetails(), modelDetails);
		else
			businessAccountDetail = getBusinessAccountDetailForUpdate(subledger);
	}

	public BusinessAccountSubLedgerDetails toDomainModel() {

		BusinessAccountDetails accountDetails = BusinessAccountDetails.builder().id(businessAccountDetail.getId())
				.build();
		return BusinessAccountSubLedgerDetails.builder().accountDetailKey(accountDetailKey)
				.accountDetailType(accountDetailType).amount(amount).id(id).tenantId(tenantId)
				.businessAccountDetail(accountDetails).build();
	}

	private BusinessAccountDetails getBusinessAccountDetailForUpdate(BusinessAccountSubLedger subledger) {

		return BusinessAccountDetails.builder().id(subledger.getBusinessAccountDetails().getId()).build();
	}

	private BusinessAccountDetails getModelAccountDetail(
			org.egov.commons.web.contract.BusinessAccountDetails serviceAccountDetails, BusinessDetails modelDetails) {
		return BusinessAccountDetails.builder().id(serviceAccountDetails.getId())
				.amount(serviceAccountDetails.getAmount()).chartOfAccount(serviceAccountDetails.getChartOfAccounts())
				.businessDetails(modelDetails).tenantId(modelDetails.getTenantId()).build();

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
		BusinessAccountSubLedgerDetails other = (BusinessAccountSubLedgerDetails) obj;
		if (accountDetailKey == null) {
			if (other.accountDetailKey != null)
				return false;
		} else if (!accountDetailKey.equals(other.accountDetailKey))
			return false;
		if (accountDetailType == null) {
			if (other.accountDetailType != null)
				return false;
		} else if (!accountDetailType.equals(other.accountDetailType))
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
		if (businessAccountDetail == null) {
			if (other.businessAccountDetail != null)
				return false;
		} else if (!businessAccountDetail.equals(other.businessAccountDetail))
			return false;

		return true;

	}

}
