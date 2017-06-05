package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SchemeContract;
import org.egov.egf.persistence.queue.contract.SchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SchemeContractResponse;
import org.egov.egf.persistence.repository.SchemeJpaRepository;
import org.egov.egf.persistence.repository.SchemeQueueRepository;
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

	private final SchemeJpaRepository schemeJpaRepository;
	private final SchemeQueueRepository schemeQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private FundService fundService;

	@Autowired
	public SchemeService(final SchemeJpaRepository schemeJpaRepository,
			final SchemeQueueRepository schemeQueueRepository) {
		this.schemeJpaRepository = schemeJpaRepository;
		this.schemeQueueRepository = schemeQueueRepository;
	}

	public void push(final SchemeContractRequest financialYearContractRequest) {
		schemeQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public SchemeContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final SchemeContractRequest schemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SchemeCreate"), SchemeContractRequest.class);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (schemeContractRequest.getSchemes() != null && !schemeContractRequest.getSchemes().isEmpty()) {
			for (SchemeContract schemeContract : schemeContractRequest.getSchemes()) {
				Scheme schemeEntity = new Scheme(schemeContract);
				schemeJpaRepository.save(schemeEntity);
				SchemeContract resp = modelMapper.map(schemeEntity, SchemeContract.class);
				schemeContractResponse.getSchemes().add(resp);
			}
		} else if (schemeContractRequest.getScheme() != null) {
			Scheme schemeEntity = new Scheme(schemeContractRequest.getScheme());
			schemeJpaRepository.save(schemeEntity);
			SchemeContract resp = modelMapper.map(schemeEntity, SchemeContract.class);
			schemeContractResponse.setScheme(resp);
		}
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		return schemeContractResponse;
	}
	
	@Transactional
	public SchemeContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final SchemeContractRequest schemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SchemeUpdateAll"), SchemeContractRequest.class);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (schemeContractRequest.getSchemes() != null && !schemeContractRequest.getSchemes().isEmpty()) {
			for (SchemeContract schemeContract : schemeContractRequest.getSchemes()) {
				Scheme schemeEntity = new Scheme(schemeContract);
				schemeEntity.setVersion(findOne(schemeEntity.getId()).getVersion());
				schemeJpaRepository.save(schemeEntity);
				SchemeContract resp = modelMapper.map(schemeEntity, SchemeContract.class);
				schemeContractResponse.getSchemes().add(resp);
			}
		}
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		return schemeContractResponse;
	}
	

	@Transactional
	public SchemeContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final SchemeContractRequest schemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SchemeUpdate"), SchemeContractRequest.class);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Scheme schemeEntity = new Scheme(schemeContractRequest.getScheme());
		schemeEntity.setVersion(schemeJpaRepository.findOne(schemeEntity.getId()).getVersion());
		schemeJpaRepository.save(schemeEntity);
		SchemeContract resp = modelMapper.map(schemeEntity, SchemeContract.class);
		schemeContractResponse.setScheme(resp);
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		return schemeContractResponse;
	}

	@Transactional
	public Scheme create(final Scheme scheme) {
		setScheme(scheme);
		return schemeJpaRepository.save(scheme);
	}

	private void setScheme(final Scheme scheme) {
		if (scheme.getFund() != null) {
			Fund fund = fundService.findOne(scheme.getFund());
			if (fund == null) {
				throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
			}
			scheme.setFund(fund.getId());
		}
	}

	@Transactional
	public Scheme update(final Scheme scheme) {
		setScheme(scheme);
		return schemeJpaRepository.save(scheme);
	}

	public List<Scheme> findAll() {
		return schemeJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Scheme findByName(String name) {
		return schemeJpaRepository.findByName(name);
	}

	public Scheme findByCode(String code) {
		return schemeJpaRepository.findByCode(code);
	}

	public Scheme findOne(Long id) {
		return schemeJpaRepository.findOne(id);
	}

	public Page<Scheme> search(SchemeContractRequest schemeContractRequest) {
		final SchemeSpecification specification = new SchemeSpecification(schemeContractRequest.getScheme());
		Pageable page = new PageRequest(schemeContractRequest.getPage().getOffSet(),
				schemeContractRequest.getPage().getPageSize());
		return schemeJpaRepository.findAll(specification, page);
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

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}