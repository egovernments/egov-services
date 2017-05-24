package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FinancialConfigurationValues;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationValuesContract;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationValuesContractRequest;
import org.egov.egf.persistence.repository.FinancialConfigurationValuesJpaRepository;
import org.egov.egf.persistence.specification.FinancialConfigurationValuesSpecification;
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
public class FinancialConfigurationValuesService {

	private final FinancialConfigurationValuesJpaRepository financialConfigurationValuesJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public FinancialConfigurationValuesService(
			final FinancialConfigurationValuesJpaRepository financialConfigurationValuesJpaRepository) {
		this.financialConfigurationValuesJpaRepository = financialConfigurationValuesJpaRepository;
	}

	@Transactional
	public FinancialConfigurationValues create(final FinancialConfigurationValues financialConfigurationValues) {
		return financialConfigurationValuesJpaRepository.save(financialConfigurationValues);
	}

	@Transactional
	public FinancialConfigurationValues update(final FinancialConfigurationValues financialConfigurationValues) {
		return financialConfigurationValuesJpaRepository.save(financialConfigurationValues);
	}

	public List<FinancialConfigurationValues> findAll() {
		return financialConfigurationValuesJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FinancialConfigurationValues findOne(Long id) {
		return financialConfigurationValuesJpaRepository.findOne(id);
	}

	public Page<FinancialConfigurationValues> search(
			FinancialConfigurationValuesContractRequest financialConfigurationValuesContractRequest) {
		final FinancialConfigurationValuesSpecification specification = new FinancialConfigurationValuesSpecification(
				financialConfigurationValuesContractRequest.getFinancialConfigurationValues());
		Pageable page = new PageRequest(financialConfigurationValuesContractRequest.getPage().getOffSet(),
				financialConfigurationValuesContractRequest.getPage().getPageSize());
		return financialConfigurationValuesJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(
			FinancialConfigurationValuesContractRequest financialConfigurationValuesContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(financialConfigurationValuesContractRequest.getFinancialConfigurationValues(),
						"FinancialConfigurationValues to edit must not be null");
				validator.validate(financialConfigurationValuesContractRequest.getFinancialConfigurationValues(),
						errors);
				break;
			case "view":
				// validator.validate(financialConfigurationValuesContractRequest.getFinancialConfigurationValues(),
				// errors);
				break;
			case "create":
				Assert.notNull(financialConfigurationValuesContractRequest.getFinancialConfigurationValueses(),
						"FinancialConfigurationValuess to create must not be null");
				for (FinancialConfigurationValuesContract b : financialConfigurationValuesContractRequest
						.getFinancialConfigurationValueses()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(financialConfigurationValuesContractRequest.getFinancialConfigurationValueses(),
						"FinancialConfigurationValuess to create must not be null");
				for (FinancialConfigurationValuesContract b : financialConfigurationValuesContractRequest
						.getFinancialConfigurationValueses()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(financialConfigurationValuesContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FinancialConfigurationValuesContractRequest fetchRelatedContracts(
			FinancialConfigurationValuesContractRequest financialConfigurationValuesContractRequest) {
		return financialConfigurationValuesContractRequest;
	}

}