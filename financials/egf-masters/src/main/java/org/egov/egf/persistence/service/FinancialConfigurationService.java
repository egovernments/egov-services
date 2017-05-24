package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FinancialConfiguration;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContract;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContractRequest;
import org.egov.egf.persistence.repository.FinancialConfigurationJpaRepository;
import org.egov.egf.persistence.specification.FinancialConfigurationSpecification;
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
public class FinancialConfigurationService {

	private final FinancialConfigurationJpaRepository financialConfigurationJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public FinancialConfigurationService(
			final FinancialConfigurationJpaRepository financialConfigurationJpaRepository) {
		this.financialConfigurationJpaRepository = financialConfigurationJpaRepository;
	}

	@Transactional
	public FinancialConfiguration create(final FinancialConfiguration financialConfiguration) {
		return financialConfigurationJpaRepository.save(financialConfiguration);
	}

	@Transactional
	public FinancialConfiguration update(final FinancialConfiguration financialConfiguration) {
		return financialConfigurationJpaRepository.save(financialConfiguration);
	}

	public List<FinancialConfiguration> findAll() {
		return financialConfigurationJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FinancialConfiguration findOne(Long id) {
		return financialConfigurationJpaRepository.findOne(id);
	}

	public Page<FinancialConfiguration> search(
			FinancialConfigurationContractRequest financialConfigurationContractRequest) {
		final FinancialConfigurationSpecification specification = new FinancialConfigurationSpecification(
				financialConfigurationContractRequest.getFinancialConfiguration());
		Pageable page = new PageRequest(financialConfigurationContractRequest.getPage().getOffSet(),
				financialConfigurationContractRequest.getPage().getPageSize());
		return financialConfigurationJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FinancialConfigurationContractRequest financialConfigurationContractRequest,
			String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(financialConfigurationContractRequest.getFinancialConfiguration(),
						"FinancialConfiguration to edit must not be null");
				validator.validate(financialConfigurationContractRequest.getFinancialConfiguration(), errors);
				break;
			case "view":
				// validator.validate(financialConfigurationContractRequest.getFinancialConfiguration(),
				// errors);
				break;
			case "create":
				Assert.notNull(financialConfigurationContractRequest.getFinancialConfigurations(),
						"FinancialConfigurations to create must not be null");
				for (FinancialConfigurationContract b : financialConfigurationContractRequest
						.getFinancialConfigurations()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(financialConfigurationContractRequest.getFinancialConfigurations(),
						"FinancialConfigurations to create must not be null");
				for (FinancialConfigurationContract b : financialConfigurationContractRequest
						.getFinancialConfigurations()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(financialConfigurationContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FinancialConfigurationContractRequest fetchRelatedContracts(
			FinancialConfigurationContractRequest financialConfigurationContractRequest) {
		return financialConfigurationContractRequest;
	}

}