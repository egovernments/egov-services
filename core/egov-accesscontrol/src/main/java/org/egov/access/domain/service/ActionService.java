package org.egov.access.domain.service;

import org.egov.access.domain.criteria.ValidateActionCriteria;
import org.egov.access.domain.model.Action;
import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.ActionFinderQueryBuilder;
import org.egov.access.persistence.repository.querybuilder.ValidateActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.egov.access.persistence.repository.rowmapper.ActionValidationRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActionService {

    private BaseRepository repository;

    public ActionService(BaseRepository actionRepository) {
        this.repository = actionRepository;
    }

    public List<Action> getActions(ActionSearchCriteria actionSearchCriteria) {
        ActionFinderQueryBuilder queryBuilder = new ActionFinderQueryBuilder(actionSearchCriteria);
        return (List<Action>) (List<?>) repository.run(queryBuilder, new ActionRowMapper());
    }

    public ActionValidation validate(ValidateActionCriteria criteria) {
        ValidateActionQueryBuilder queryBuilder = new ValidateActionQueryBuilder(criteria);
        return (ActionValidation) repository.run(queryBuilder, new ActionValidationRowMapper()).get(0);
    }
}
