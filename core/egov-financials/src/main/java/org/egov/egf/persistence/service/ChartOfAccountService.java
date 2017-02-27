package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractRequest;
import org.egov.egf.persistence.repository.ChartOfAccountRepository;
import org.egov.egf.persistence.specification.ChartOfAccountSpecification;
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
public class ChartOfAccountService {

	private final ChartOfAccountRepository chartOfAccountRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public ChartOfAccountService(final ChartOfAccountRepository chartOfAccountRepository) {
		this.chartOfAccountRepository = chartOfAccountRepository;
	}

	@Autowired
	private SmartValidator validator;

	@Transactional
	public ChartOfAccount create(final ChartOfAccount chartOfAccount) {
		return chartOfAccountRepository.save(chartOfAccount);
	}

	@Transactional
	public ChartOfAccount update(final ChartOfAccount chartOfAccount) {
		return chartOfAccountRepository.save(chartOfAccount);
	}

	public List<ChartOfAccount> findAll() {
		return chartOfAccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public ChartOfAccount findByName(String name) {
		return chartOfAccountRepository.findByName(name);
	}

	public ChartOfAccount findOne(Long id) {
		return chartOfAccountRepository.findOne(id);
	}

	public Page<ChartOfAccount> search(ChartOfAccountContractRequest chartOfAccountContractRequest) {
		final ChartOfAccountSpecification specification = new ChartOfAccountSpecification(
				chartOfAccountContractRequest.getChartOfAccount());
		Pageable page = new PageRequest(chartOfAccountContractRequest.getPage().getOffSet(),
				chartOfAccountContractRequest.getPage().getPageSize());
		return chartOfAccountRepository.findAll(specification, page);
	}

	public BindingResult validate(ChartOfAccountContractRequest chartOfAccountContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccount(),
						"ChartOfAccount to edit must not be null");
				validator.validate(chartOfAccountContractRequest.getChartOfAccount(), errors);
				break;
			case "view":
				// validator.validate(chartOfAccountContractRequest.getChartOfAccount(),
				// errors);
				break;
			case "create":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
						"ChartOfAccounts to create must not be null");
				for (ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
						"ChartOfAccounts to create must not be null");
				for (ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(chartOfAccountContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}
}