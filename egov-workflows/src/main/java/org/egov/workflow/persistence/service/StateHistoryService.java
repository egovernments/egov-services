package org.egov.workflow.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.repository.StateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class StateHistoryService {

	private final StateHistoryRepository stateHistoryRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public StateHistoryService(final StateHistoryRepository stateHistoryRepository) {
		this.stateHistoryRepository = stateHistoryRepository;
	}

	@Autowired
	private SmartValidator validator;
	@Autowired
	private StateService stateService;

	@Transactional
	public StateHistory create(final StateHistory stateHistory) {
		return stateHistoryRepository.save(stateHistory);
	}

	@Transactional
	public StateHistory update(final StateHistory stateHistory) {
		return stateHistoryRepository.save(stateHistory);
	}

	public List<StateHistory> findAll() {
		return stateHistoryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public StateHistory findOne(Long id) {
		return stateHistoryRepository.findOne(id);
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