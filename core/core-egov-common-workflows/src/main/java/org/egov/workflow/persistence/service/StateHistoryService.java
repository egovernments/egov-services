package org.egov.workflow.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.repository.StateHistoryJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StateHistoryService {

	private final StateHistoryJdbcRepository stateHistoryJdbcRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public StateHistoryService(final StateHistoryJdbcRepository stateHistoryJdbcRepository) {
		this.stateHistoryJdbcRepository = stateHistoryJdbcRepository;
	}

	@Transactional
	public StateHistory create(final StateHistory stateHistory) {
		return stateHistoryJdbcRepository.create(stateHistory);
	}

	@Transactional
	public StateHistory update(final StateHistory stateHistory) {
		return stateHistoryJdbcRepository.update(stateHistory);
	}

	public List<StateHistory> findAll(final StateHistory stateHistory) {
		return stateHistoryJdbcRepository.search(stateHistory);
	}

	public StateHistory findOne(final StateHistory stateHistory) {
		return stateHistoryJdbcRepository.findById(stateHistory);
	}

	/*public Page<StateHistory> search(StateHistoryContractRequest stateHistoryContractRequest) {
		final StateHistorySpecification specification = new StateHistorySpecification(
				stateHistoryContractRequest.getStateHistory());
		Pageable page = new PageRequest(stateHistoryContractRequest.getPage().getOffSet(),
				stateHistoryContractRequest.getPage().getPageSize());
		return stateHistoryRepository.findAll(specification, page);
	}

	public BindingResult validate(StateHistoryContractRequest stateHistoryContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(stateHistoryContractRequest.getStateHistory(), "StateHistory to edit must not be null");
				validator.validate(stateHistoryContractRequest.getStateHistory(), errors);
				break;
			case "view":
				// validator.validate(stateHistoryContractRequest.getStateHistory(),
				// errors);
				break;
			case "create":
				Assert.notNull(stateHistoryContractRequest.getStateHistories(),
						"StateHistories to create must not be null");
				for (StateHistoryContract b : stateHistoryContractRequest.getStateHistories())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(stateHistoryContractRequest.getStateHistories(),
						"StateHistories to create must not be null");
				for (StateHistoryContract b : stateHistoryContractRequest.getStateHistories())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(stateHistoryContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public StateHistoryContractRequest fetchRelatedContracts(StateHistoryContractRequest stateHistoryContractRequest) {
		ModelMapper model = new ModelMapper();
		for (StateHistoryContract stateHistory : stateHistoryContractRequest.getStateHistories()) {
			if (stateHistory.getState() != null) {
				State state = stateService.findOne(stateHistory.getState().getId());
				if (state == null) {
					throw new InvalidDataException("state", "state.invalid", " Invalid state");
				}
				model.map(state, stateHistory.getState());
			}
		}
		StateHistoryContract stateHistory = stateHistoryContractRequest.getStateHistory();
		if (stateHistory.getState() != null) {
			State state = stateService.findOne(stateHistory.getState().getId());
			if (state == null) {
				throw new InvalidDataException("state", "state.invalid", " Invalid state");
			}
			model.map(state, stateHistory.getState());
		}
		return stateHistoryContractRequest;
	}*/
}