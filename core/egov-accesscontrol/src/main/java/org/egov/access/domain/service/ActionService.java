package org.egov.access.domain.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.criteria.ValidateActionCriteria;
import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.persistence.repository.ActionRepository;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.ActionFinderQueryBuilder;
import org.egov.access.persistence.repository.querybuilder.ValidateActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.egov.access.persistence.repository.rowmapper.ActionValidationRowMapper;
import org.egov.access.web.contract.action.ActionRequest;
import org.egov.access.web.contract.action.Module;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

	private BaseRepository repository;

	private ActionRepository actionRepository;

	@Autowired
	public ActionService(BaseRepository repository, ActionRepository actionRepository) {
		this.repository = repository;
		this.actionRepository = actionRepository;
	}

	public List<Action> getActions(ActionSearchCriteria actionSearchCriteria) {
		ActionFinderQueryBuilder queryBuilder = new ActionFinderQueryBuilder(actionSearchCriteria);
		return (List<Action>) (List<?>) repository.run(queryBuilder, new ActionRowMapper());
	}

	public ActionValidation validate(ValidateActionCriteria criteria) {
		ValidateActionQueryBuilder queryBuilder = new ValidateActionQueryBuilder(criteria);
		return (ActionValidation) repository.run(queryBuilder, new ActionValidationRowMapper()).get(0);
	}

	public List<Action> createAction(ActionRequest actionRequest) {

		return actionRepository.createAction(actionRequest);
	}

	public List<Action> updateAction(ActionRequest actionRequest) {

		return actionRepository.updateAction(actionRequest);
	}

	public boolean checkActionNameExit(String name) {

		return actionRepository.checkActionNameExit(name);
	}

	public boolean checkCombinationOfUrlAndqueryparamsExist(String url, String queryParams) {

		return actionRepository.checkCombinationOfUrlAndqueryparamsExist(url, queryParams);
	}

	public List<Module> getAllActionsBasedOnRoles(final ActionRequest actionRequest) {

		return actionRepository.getAllActionsBasedOnRoles(actionRequest).getModules();

	}

	public List<Action> getAllActions(final ActionRequest actionRequest) {
         
		return actionRepository.getAllActions(actionRequest);
		
	}
	public List<Action> getAllMDMSActions(final ActionRequest actionRequest) throws JSONException, UnsupportedEncodingException{
        
		return actionRepository.getAllMDMSActions(actionRequest);
	}
}
