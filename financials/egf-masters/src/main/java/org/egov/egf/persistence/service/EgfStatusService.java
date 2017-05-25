package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.EgfStatus;
import org.egov.egf.persistence.queue.contract.EgfStatusContract;
import org.egov.egf.persistence.queue.contract.EgfStatusContractRequest;
import org.egov.egf.persistence.repository.EgfStatusJpaRepository;
import org.egov.egf.persistence.specification.EgfStatusSpecification;
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
public class EgfStatusService {

	private final EgfStatusJpaRepository egfStatusJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public EgfStatusService(final EgfStatusJpaRepository egfStatusJpaRepository) {
		this.egfStatusJpaRepository = egfStatusJpaRepository;
	}

	@Transactional
	public EgfStatus create(final EgfStatus egfStatus) {
		return egfStatusJpaRepository.save(egfStatus);
	}

	@Transactional
	public EgfStatus update(final EgfStatus egfStatus) {
		return egfStatusJpaRepository.save(egfStatus);
	}

	public List<EgfStatus> findAll() {
		return egfStatusJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public EgfStatus findOne(Long id) {
		return egfStatusJpaRepository.findOne(id);
	}

	public Page<EgfStatus> search(EgfStatusContractRequest egfStatusContractRequest) {
		final EgfStatusSpecification specification = new EgfStatusSpecification(
				egfStatusContractRequest.getEgfStatus());
		Pageable page = new PageRequest(egfStatusContractRequest.getPage().getOffSet(),
				egfStatusContractRequest.getPage().getPageSize());
		return egfStatusJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(EgfStatusContractRequest egfStatusContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(egfStatusContractRequest.getEgfStatus(), "EgfStatus to edit must not be null");
				validator.validate(egfStatusContractRequest.getEgfStatus(), errors);
				break;
			case "view":
				// validator.validate(egfStatusContractRequest.getEgfStatus(),
				// errors);
				break;
			case "create":
				Assert.notNull(egfStatusContractRequest.getEgfStatuses(), "EgfStatuss to create must not be null");
				for (EgfStatusContract b : egfStatusContractRequest.getEgfStatuses()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(egfStatusContractRequest.getEgfStatuses(), "EgfStatuss to create must not be null");
				for (EgfStatusContract b : egfStatusContractRequest.getEgfStatuses()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(egfStatusContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public EgfStatusContractRequest fetchRelatedContracts(EgfStatusContractRequest egfStatusContractRequest) {
		return egfStatusContractRequest;
	}

}