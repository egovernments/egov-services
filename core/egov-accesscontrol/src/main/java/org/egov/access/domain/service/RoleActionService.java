package org.egov.access.domain.service;

import java.util.List;

import org.egov.access.domain.model.RoleAction;
import org.egov.access.persistence.repository.RoleActionRepository;
import org.egov.access.web.contract.action.RoleActionsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleActionService {

	@Autowired
	private RoleActionRepository actionRepository;

   public List<RoleAction> createRoleActions(final RoleActionsRequest rolActionRequest){
    	
    	return actionRepository.createRoleActions(rolActionRequest);
    }
	
	

}


