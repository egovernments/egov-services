package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.EgfConfigurationValues;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContract;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContractRequest;
import org.egov.egf.persistence.repository.EgfConfigurationValuesJpaRepository;
import org.egov.egf.persistence.specification.EgfConfigurationValuesSpecification;
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
public class EgfConfigurationValuesService {

	private final EgfConfigurationValuesJpaRepository egfConfigurationValuesJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public EgfConfigurationValuesService(
			final EgfConfigurationValuesJpaRepository egfConfigurationValuesJpaRepository) {
		this.egfConfigurationValuesJpaRepository = egfConfigurationValuesJpaRepository;
	}

	@Transactional
	public EgfConfigurationValues create(final EgfConfigurationValues egfConfigurationValues) {
		return egfConfigurationValuesJpaRepository.save(egfConfigurationValues);
	}

	@Transactional
	public EgfConfigurationValues update(final EgfConfigurationValues egfConfigurationValues) {
		return egfConfigurationValuesJpaRepository.save(egfConfigurationValues);
	}

	public List<EgfConfigurationValues> findAll() {
		return egfConfigurationValuesJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public EgfConfigurationValues findOne(Long id) {
		return egfConfigurationValuesJpaRepository.findOne(id);
	}

	public Page<EgfConfigurationValues> search(
			EgfConfigurationValuesContractRequest egfConfigurationValuesContractRequest) {
		final EgfConfigurationValuesSpecification specification = new EgfConfigurationValuesSpecification(
				egfConfigurationValuesContractRequest.getEgfConfigurationValues());
		Pageable page = new PageRequest(egfConfigurationValuesContractRequest.getPage().getOffSet(),
				egfConfigurationValuesContractRequest.getPage().getPageSize());
		return egfConfigurationValuesJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(EgfConfigurationValuesContractRequest egfConfigurationValuesContractRequest,
			String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(egfConfigurationValuesContractRequest.getEgfConfigurationValues(),
						"EgfConfigurationValues to edit must not be null");
				validator.validate(egfConfigurationValuesContractRequest.getEgfConfigurationValues(), errors);
				break;
			case "view":
				// validator.validate(egfConfigurationValuesContractRequest.getEgfConfigurationValues(),
				// errors);
				break;
			case "create":
				Assert.notNull(egfConfigurationValuesContractRequest.getEgfConfigurationValueses(),
						"EgfConfigurationValuess to create must not be null");
				for (EgfConfigurationValuesContract b : egfConfigurationValuesContractRequest
						.getEgfConfigurationValueses()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(egfConfigurationValuesContractRequest.getEgfConfigurationValueses(),
						"EgfConfigurationValuess to create must not be null");
				for (EgfConfigurationValuesContract b : egfConfigurationValuesContractRequest
						.getEgfConfigurationValueses()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(egfConfigurationValuesContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public EgfConfigurationValuesContractRequest fetchRelatedContracts(
			EgfConfigurationValuesContractRequest egfConfigurationValuesContractRequest) {
		return egfConfigurationValuesContractRequest;
	}

}