package org.egov.commons.service;

import java.util.List;

import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessAccountSubLedgerDetails;
import org.egov.commons.model.BusinessDetails;
import org.egov.commons.model.BusinessDetailsCommonModel;
import org.egov.commons.model.BusinessDetailsCriteria;
import org.egov.commons.repository.BusinessDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessDetailsService {

	@Autowired
	private BusinessDetailsRepository businessDetailsRepository;

	public BusinessDetailsCommonModel createBusinessDetails(BusinessDetails modelDetails,
			List<org.egov.commons.model.BusinessAccountDetails> listModelAccountDetails,
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger, AuthenticatedUser user) {
		return businessDetailsRepository.createBusinessDetails(modelDetails, listModelAccountDetails,
				listModelAccountSubledger, user);

	}

	public BusinessDetailsCommonModel updateBusinessDetails(BusinessDetails modelDetails,
			List<org.egov.commons.model.BusinessAccountDetails> listModelAccountDetails,
			List<BusinessAccountSubLedgerDetails> listModelAccountSubledger, AuthenticatedUser user) {
		return businessDetailsRepository.updateBusinessDetails(modelDetails, listModelAccountDetails,
				listModelAccountSubledger, user);

	}

	public BusinessDetailsCommonModel getForCriteria(BusinessDetailsCriteria detailsCriteria) {
		return businessDetailsRepository.getForCriteria(detailsCriteria);
	}

	public boolean getBusinessDetailsByNameAndTenantId(String name, String tenantId) {
		return businessDetailsRepository.checkDetailsByNameAndTenantIdExists(name, tenantId);
	}

	public boolean getBusinessDetailsByCodeAndTenantId(String code, String tenantId) {
		return businessDetailsRepository.checkDetailsByCodeAndTenantIdExists(code, tenantId);
	}

}
