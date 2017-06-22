package org.egov.demand.web.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.demand.model.Demand;
import org.egov.demand.repository.OwnerRepository;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.UserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DemandValidator implements Validator {

	@Autowired
	private OwnerRepository ownerRepository;

	@Override
	public boolean supports(Class<?> clazz) {

		return DemandRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		DemandRequest demandRequest = null;
		if (target instanceof DemandRequest)
			demandRequest = (DemandRequest) target;
		else
			throw new RuntimeException("Invalid Object type for Demand validator");
		validateOwner(demandRequest, errors);
	}

	private void validateOwner(DemandRequest demandRequest, Errors errors) {

		List<Demand> demands = demandRequest.getDemands();
		List<Long> ownerIds = new ArrayList<>(
				demands.stream().map(demand -> demand.getOwner().getId()).collect(Collectors.toSet()));
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setId(ownerIds);
		List<Long> rsIds = ownerRepository.getOwners(userSearchRequest).stream().map(owner -> owner.getId())
				.collect(Collectors.toList());
		for (Long rsId : ownerIds) {
			boolean isIdPresent = false;
			for (Long long1 : rsIds) {
				if(rsId.equals(long1))
					isIdPresent = true;
			}
			if(!isIdPresent)
				errors.rejectValue("Demand.Owner,id","","the given user id value '"+rsId+"' is invalid, please give a valid id");
		}

	}

}
