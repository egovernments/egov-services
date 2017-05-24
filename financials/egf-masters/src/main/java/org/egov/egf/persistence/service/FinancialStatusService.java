package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FinancialStatus;
import org.egov.egf.persistence.queue.contract.FinancialStatusContract;
import org.egov.egf.persistence.queue.contract.FinancialStatusContractRequest;
import org.egov.egf.persistence.repository.FinancialStatusJpaRepository;
import org.egov.egf.persistence.specification.FinancialStatusSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FinancialStatusService {

	private final FinancialStatusJpaRepository financialStatusJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public FinancialStatusService(final FinancialStatusJpaRepository financialStatusJpaRepository) {
		this.financialStatusJpaRepository = financialStatusJpaRepository;
	}

	@Transactional
	public FinancialStatus create(final FinancialStatus financialStatus) {
		return financialStatusJpaRepository.save(financialStatus);
	}

	@Transactional
	public FinancialStatus update(final FinancialStatus financialStatus) {
		return financialStatusJpaRepository.save(financialStatus);
	}

	public List<FinancialStatus> findAll() {
		return financialStatusJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FinancialStatus findOne(Long id) {
		return financialStatusJpaRepository.findOne(id);
	}

	public Page<FinancialStatus> search(FinancialStatusContractRequest financialStatusContractRequest) {
		final FinancialStatusSpecification specification = new FinancialStatusSpecification(
				financialStatusContractRequest.getFinancialStatus());
		Pageable page = new PageRequest(financialStatusContractRequest.getPage().getOffSet(),
				financialStatusContractRequest.getPage().getPageSize());
		return financialStatusJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FinancialStatusContractRequest financialStatusContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(financialStatusContractRequest.getFinancialStatus(),
						"FinancialStatus to edit must not be null");
				validator.validate(financialStatusContractRequest.getFinancialStatus(), errors);
				break;
			case "view":
				// validator.validate(financialStatusContractRequest.getFinancialStatus(),
				// errors);
				break;
			case "create":
				Assert.notNull(financialStatusContractRequest.getFinancialStatuses(),
						"FinancialStatuss to create must not be null");
				for (FinancialStatusContract b : financialStatusContractRequest.getFinancialStatuses()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(financialStatusContractRequest.getFinancialStatuses(),
						"FinancialStatuss to create must not be null");
				for (FinancialStatusContract b : financialStatusContractRequest.getFinancialStatuses()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(financialStatusContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FinancialStatusContractRequest fetchRelatedContracts(
			FinancialStatusContractRequest financialStatusContractRequest) {
		return financialStatusContractRequest;
	}

}