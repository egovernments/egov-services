package org.egov.workflow.persistence.service;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.repository.StateJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StateService {

	private final StateJdbcRepository stateJdbcRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public StateService(final StateJdbcRepository stateJdbcRepository) {
		this.stateJdbcRepository = stateJdbcRepository;
	}
/*fffffffffffffffffffffffffff*/
	@Transactional
	public State create(final State state) {
			state.setId(Long.parseLong(stateJdbcRepository.getNextSequence()));
		
		return stateJdbcRepository.create(state);
	}
	
	public List<State> fetchRelated(List<State> banks) {

		return banks;
	}

	@Transactional
	public State update(final State state) {
		return stateJdbcRepository.update(state);
	}

	public List<State> findAll(final State state) {
		return stateJdbcRepository.search(state);
	}
	
	public State findByIdAndTenantId(final State state) {
		return stateJdbcRepository.findById(state);
	}

	/*public State  findOne(final ProcessInstance state) {
		return stateJdbcRepository.findByIds(state);
	}*/
	

	public List<State> getStates(List<Long> ownerIds, List<String> types, Long userId, String tenantId) {

		// As of now we have handled for EMPLOYEE. We need to handle for NON-EMPLOYEE also.
		State states=State.builder()
						.tenantId(tenantId)
						.owner_user(userId)
						.owner_pos(ownerIds.get(0))
						.type(types.get(0))
						.build();
		if(ownerIds != null && ! ownerIds.isEmpty())
		{
		return  stateJdbcRepository.search(states);
		}
		/*if (ownerIds != null && !ownerIds.isEmpty())
			return getSession()
					.createCriteria(State.class)
					.setFlushMode(FlushMode.MANUAL)
					.setReadOnly(true)
					.setCacheable(true)
					.add(Restrictions.in("type", types))
					.add(Restrictions.in("ownerPosition", ownerIds))
					.add(Restrictions.eq("tenantId", tenantId))
					.add(Restrictions.ne("status", StateStatus.ENDED))
					.add(Restrictions.not(Restrictions.conjunction()
					.add(Restrictions.eq("status", StateStatus.STARTED))
					.add(Restrictions.eq("createdBy", userId))))
					.addOrder(Order.desc("createdDate"))
					.list();*/
		else
			return Collections.emptyList();

	}

	/*private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
*/
	/*
	 * public Page<State> search(StateContractRequest stateContractRequest) {
	 * final StateSpecification specification = new
	 * StateSpecification(stateContractRequest.getState()); Pageable page = new
	 * PageRequest(stateContractRequest.getPage().getOffSet(),
	 * stateContractRequest.getPage().getPageSize()); return
	 * stateJdbcRepository.findAll(specification, page); }
	 * 
	 * public BindingResult validate(StateContractRequest stateContractRequest,
	 * String method, BindingResult errors) {
	 * 
	 * try { switch (method) { case "update":
	 * Assert.notNull(stateContractRequest.getState(),
	 * "State to edit must not be null");
	 * validator.validate(stateContractRequest.getState(), errors); break; case
	 * "view": // validator.validate(stateContractRequest.getState(), errors);
	 * break; case "create": Assert.notNull(stateContractRequest.getStates(),
	 * "States to create must not be null"); for (StateContract b :
	 * stateContractRequest.getStates()) validator.validate(b, errors); break;
	 * case "updateAll": Assert.notNull(stateContractRequest.getStates(),
	 * "States to create must not be null"); for (StateContract b :
	 * stateContractRequest.getStates()) validator.validate(b, errors); break;
	 * default: validator.validate(stateContractRequest.getRequestInfo(),
	 * errors); } } catch (IllegalArgumentException e) { errors.addError(new
	 * ObjectError("Missing data", e.getMessage())); } return errors;
	 * 
	 * }
	 * 
	 * public StateContractRequest fetchRelatedContracts(StateContractRequest
	 * stateContractRequest) { ModelMapper model = new ModelMapper(); for
	 * (StateContract state : stateContractRequest.getStates()) { if
	 * (state.getStatus() != null) { StateStatus status =
	 * stateStatusService.findOne(state.getStatus().getId()); if (status ==
	 * null) { throw new InvalidDataException("status", "status.invalid",
	 * " Invalid status"); } model.map(status, state.getStatus()); } }
	 * StateContract state = stateContractRequest.getState(); if
	 * (state.getStatus() != null) { StateStatus status =
	 * stateStatusService.findOne(state.getStatus().getId()); if (status ==
	 * null) { throw new InvalidDataException("status", "status.invalid",
	 * " Invalid status"); } model.map(status, state.getStatus()); } return
	 * stateContractRequest; }
	 */
}