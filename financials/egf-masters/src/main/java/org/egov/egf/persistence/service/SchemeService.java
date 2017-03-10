package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.queue.contract.SchemeContract;
import org.egov.egf.persistence.queue.contract.SchemeContractRequest;
import org.egov.egf.persistence.repository.SchemeRepository;
import org.egov.egf.persistence.specification.SchemeSpecification;
import org.modelmapper.ModelMapper;
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
public class SchemeService {

	private final SchemeRepository schemeRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public SchemeService(final SchemeRepository schemeRepository) {
		this.schemeRepository = schemeRepository;
	}

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FundService fundService;

	@Transactional
	public Scheme create(final Scheme scheme) {
		return schemeRepository.save(scheme);
	}

	@Transactional
	public Scheme update(final Scheme scheme) {
		return schemeRepository.save(scheme);
	}

	public List<Scheme> findAll() {
		return schemeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Scheme findByName(String name) {
		return schemeRepository.findByName(name);
	}

	public Scheme findByCode(String code) {
		return schemeRepository.findByCode(code);
	}

	public Scheme findOne(Long id) {
		return schemeRepository.findOne(id);
	}

	public Page<Scheme> search(SchemeContractRequest schemeContractRequest) {
		final SchemeSpecification specification = new SchemeSpecification(schemeContractRequest.getScheme());
		Pageable page = new PageRequest(schemeContractRequest.getPage().getOffSet(),
				schemeContractRequest.getPage().getPageSize());
		return schemeRepository.findAll(specification, page);
	}

	public BindingResult validate(SchemeContractRequest schemeContractRequest, String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(schemeContractRequest.getScheme(), "Scheme to edit must not be null");
				validator.validate(schemeContractRequest.getScheme(), errors);
				break;
			case "view":
				// validator.validate(schemeContractRequest.getScheme(),
				// errors);
				break;
			case "create":
				Assert.notNull(schemeContractRequest.getSchemes(), "Schemes to create must not be null");
				for (SchemeContract b : schemeContractRequest.getSchemes()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(schemeContractRequest.getSchemes(), "Schemes to create must not be null");
				for (SchemeContract b : schemeContractRequest.getSchemes()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(schemeContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public SchemeContractRequest fetchRelatedContracts(SchemeContractRequest schemeContractRequest) {
		ModelMapper model = new ModelMapper();
		for (SchemeContract scheme : schemeContractRequest.getSchemes()) {
			if (scheme.getFund() != null) {
				Fund fund = fundService.findOne(scheme.getFund().getId());
				if (fund == null) {
					throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
				}
				model.map(fund, scheme.getFund());
			}
		}
		SchemeContract scheme = schemeContractRequest.getScheme();
		if (scheme.getFund() != null) {
			Fund fund = fundService.findOne(scheme.getFund().getId());
			if (fund == null) {
				throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
			}
			model.map(fund, scheme.getFund());
		}
		return schemeContractRequest;
	}
}