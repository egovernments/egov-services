package org.egov.commons.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.web.contract.BusinessAccountSubLedger;
import org.egov.commons.web.contract.BusinessDetailsRequestInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BusinessDetailsCommonModel {

	List<BusinessDetails> businessDetails;

	List<BusinessAccountDetails> businessAccountDetails;

	List<BusinessAccountSubLedgerDetails> businessAccountSubledgerDetails;

	public List<BusinessDetailsRequestInfo> toDomainContract() {
		List<BusinessDetailsRequestInfo> listOfDetailsInfo = new ArrayList<>();
		for (BusinessDetails details : businessDetails) {
			BusinessDetailsRequestInfo detailsRequestInfo = BusinessDetailsRequestInfo.builder().id(details.getId())
					.active(details.getIsEnabled()).code(details.getCode()).name(details.getName())
					.businessCategory(details.getBusinessCategory().getId()).businessType(details.getBusinessType())
					.businessUrl(details.getBusinessUrl()).department(details.getDepartment())
					.fundSource(details.getFundSource()).function(details.getFunction())
					.functionary(details.getFunctionary()).fund(details.getFund())
					.isVoucherApproved(details.getIsVoucherApproved()).ordernumber(details.getOrdernumber())
					.tenantId(details.getTenantId()).voucherCreation(details.getVoucherCreation())
					.voucherCutoffDate(details.getVoucherCutoffDate()).build();
			org.egov.commons.web.contract.BusinessDetails detail = org.egov.commons.web.contract.BusinessDetails
					.builder().id(details.getId()).active(details.getIsEnabled()).name(details.getName())
					.businessCategory(details.getBusinessCategory().getId()).businessType(details.getBusinessType())
					.businessUrl(details.getBusinessUrl()).department(details.getDepartment())
					.fundSource(details.getFundSource()).function(details.getFunction())
					.functionary(details.getFunctionary()).fund(details.getFund())
					.isVoucherApproved(details.getIsVoucherApproved()).ordernumber(details.getOrdernumber())
					.tenantId(details.getTenantId()).voucherCreation(details.getVoucherCreation())
					.voucherCutoffDate(details.getVoucherCutoffDate()).code(details.getCode()).build();
			List<BusinessAccountDetails> requiredBusinessAccountDetails = new ArrayList<>();
			List<org.egov.commons.web.contract.BusinessAccountDetails> contractAccountDetails = new ArrayList<>();
			for (BusinessAccountDetails accountDetails : businessAccountDetails) {
				if (accountDetails.getBusinessDetails().getId().equals(details.getId()))
					requiredBusinessAccountDetails.add(accountDetails);
			}
			for (BusinessAccountDetails requiredAccount : requiredBusinessAccountDetails) {
				contractAccountDetails.add(org.egov.commons.web.contract.BusinessAccountDetails.builder()
						.id(requiredAccount.getId()).amount(requiredAccount.getAmount())
						.chartOfAccounts(requiredAccount.getChartOfAccount()).businessDetails(detail).build());

			}
			detailsRequestInfo.setAccountDetails(contractAccountDetails);
			List<BusinessAccountSubLedger> contractAccountSubledger = new ArrayList<>();
			for (org.egov.commons.web.contract.BusinessAccountDetails account : contractAccountDetails) {
				for (BusinessAccountSubLedgerDetails subledgerDetails : businessAccountSubledgerDetails) {
					if (subledgerDetails.getBusinessAccountDetail().getId().equals(account.getId())) {
						contractAccountSubledger.add(BusinessAccountSubLedger.builder().id(subledgerDetails.getId())
								.amount(subledgerDetails.getAmount()).detailKey(subledgerDetails.getAccountDetailKey())
								.detailType(subledgerDetails.getAccountDetailType()).businessAccountDetails(account)
								.build());
					}
				}
			}
			detailsRequestInfo.setSubledgerDetails(contractAccountSubledger);
			listOfDetailsInfo.add(detailsRequestInfo);
		}
		return listOfDetailsInfo;
	}
}
