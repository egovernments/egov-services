package org.egov.access.domain.service;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.access.persistence.repository.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActionService {

    private ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getActions(ActionSearchCriteria actionSearchCriteria) {
       return actionRepository.findForCriteria(actionSearchCriteria);
    }
}
