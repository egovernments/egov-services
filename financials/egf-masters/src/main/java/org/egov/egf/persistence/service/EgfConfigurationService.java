package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.EgfConfiguration;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContract;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContractRequest;
import org.egov.egf.persistence.repository.EgfConfigurationJpaRepository;
import org.egov.egf.persistence.specification.EgfConfigurationSpecification;
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
public class EgfConfigurationService {

	private final EgfConfigurationJpaRepository egfConfigurationJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public EgfConfigurationService(final EgfConfigurationJpaRepository egfConfigurationJpaRepository) {
		this.egfConfigurationJpaRepository = egfConfigurationJpaRepository;
	}

	@Transactional
	public EgfConfiguration create(final EgfConfiguration egfConfiguration) {
		return egfConfigurationJpaRepository.save(egfConfiguration);
	}

	@Transactional
	public EgfConfiguration update(final EgfConfiguration egfConfiguration) {
		return egfConfigurationJpaRepository.save(egfConfiguration);
	}

	public List<EgfConfiguration> findAll() {
		return egfConfigurationJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public EgfConfiguration findOne(Long id) {
		return egfConfigurationJpaRepository.findOne(id);
	}

	public Page<EgfConfiguration> search(EgfConfigurationContractRequest egfConfigurationContractRequest) {
		final EgfConfigurationSpecification specification = new EgfConfigurationSpecification(
				egfConfigurationContractRequest.getEgfConfiguration());
		Pageable page = new PageRequest(egfConfigurationContractRequest.getPage().getOffSet(),
				egfConfigurationContractRequest.getPage().getPageSize());
		return egfConfigurationJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(EgfConfigurationContractRequest egfConfigurationContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(egfConfigurationContractRequest.getEgfConfiguration(),
						"EgfConfiguration to edit must not be null");
				validator.validate(egfConfigurationContractRequest.getEgfConfiguration(), errors);
				break;
			case "view":
				// validator.validate(egfConfigurationContractRequest.getEgfConfiguration(),
				// errors);
				break;
			case "create":
				Assert.notNull(egfConfigurationContractRequest.getEgfConfigurations(),
						"EgfConfigurations to create must not be null");
				for (EgfConfigurationContract b : egfConfigurationContractRequest.getEgfConfigurations()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(egfConfigurationContractRequest.getEgfConfigurations(),
						"EgfConfigurations to create must not be null");
				for (EgfConfigurationContract b : egfConfigurationContractRequest.getEgfConfigurations()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(egfConfigurationContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public EgfConfigurationContractRequest fetchRelatedContracts(
			EgfConfigurationContractRequest egfConfigurationContractRequest) {
		return egfConfigurationContractRequest;
	}

}