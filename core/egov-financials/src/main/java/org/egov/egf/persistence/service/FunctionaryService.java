package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.queue.contract.FunctionaryContract;
import org.egov.egf.persistence.queue.contract.FunctionaryContractRequest;
import org.egov.egf.persistence.repository.FunctionaryRepository;
import org.egov.egf.persistence.specification.FunctionarySpecification;
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
public class FunctionaryService {

	private final FunctionaryRepository functionaryRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public FunctionaryService(final FunctionaryRepository functionaryRepository) {
		this.functionaryRepository = functionaryRepository;
	}

	@Autowired
	private SmartValidator validator;

	@Transactional
	public Functionary create(final Functionary functionary) {
		return functionaryRepository.save(functionary);
	}

	@Transactional
	public Functionary update(final Functionary functionary) {
		return functionaryRepository.save(functionary);
	}

	public List<Functionary> findAll() {
		return functionaryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Functionary findByName(String name) {
		return functionaryRepository.findByName(name);
	}

	public Functionary findByCode(String code) {
		return functionaryRepository.findByCode(code);
	}

	public Functionary findOne(Long id) {
		return functionaryRepository.findOne(id);
	}

	public Page<Functionary> search(FunctionaryContractRequest functionaryContractRequest) {
		final FunctionarySpecification specification = new FunctionarySpecification(
				functionaryContractRequest.getFunctionary());
		Pageable page = new PageRequest(functionaryContractRequest.getPage().getOffSet(),
				functionaryContractRequest.getPage().getPageSize());
		return functionaryRepository.findAll(specification, page);
	}

	public BindingResult validate(FunctionaryContractRequest functionaryContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(functionaryContractRequest.getFunctionary(), "Functionary to edit must not be null");
				validator.validate(functionaryContractRequest.getFunctionary(), errors);
				break;
			case "view":
				// validator.validate(functionaryContractRequest.getFunctionary(),
				// errors);
				break;
			case "create":
				Assert.notNull(functionaryContractRequest.getFunctionaries(),
						"Functionaries to create must not be null");
				for (FunctionaryContract b : functionaryContractRequest.getFunctionaries()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(functionaryContractRequest.getFunctionaries(),
						"Functionaries to create must not be null");
				for (FunctionaryContract b : functionaryContractRequest.getFunctionaries()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(functionaryContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FunctionaryContractRequest fetchRelatedContracts(FunctionaryContractRequest functionaryContractRequest) {
		return functionaryContractRequest;
	}
}