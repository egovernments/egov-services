package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SubSchemeContract;
import org.egov.egf.persistence.queue.contract.SubSchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SubSchemeContractResponse;
import org.egov.egf.persistence.repository.SubSchemeJpaRepository;
import org.egov.egf.persistence.repository.SubSchemeQueueRepository;
import org.egov.egf.persistence.specification.SubSchemeSpecification;
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
public class SubSchemeService {

	private final SubSchemeJpaRepository subSchemeJpaRepository;

	private final SubSchemeQueueRepository subSchemeQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private SchemeService schemeService;

	@Autowired
	public SubSchemeService(final SubSchemeJpaRepository subSchemeJpaRepository,
			final SubSchemeQueueRepository subSchemeQueueRepository) {
		this.subSchemeJpaRepository = subSchemeJpaRepository;
		this.subSchemeQueueRepository = subSchemeQueueRepository;
	}

	public void push(final SubSchemeContractRequest financialYearContractRequest) {
		subSchemeQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public SubSchemeContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final SubSchemeContractRequest subSchemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SubSchemeCreate"), SubSchemeContractRequest.class);
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (subSchemeContractRequest.getSubSchemes() != null && !subSchemeContractRequest.getSubSchemes().isEmpty()) {
			for (SubSchemeContract subSchemeContract : subSchemeContractRequest.getSubSchemes()) {
				SubScheme subSchemeEntity = new SubScheme(subSchemeContract);
				subSchemeJpaRepository.save(subSchemeEntity);
				SubSchemeContract resp = modelMapper.map(subSchemeEntity, SubSchemeContract.class);
				subSchemeContractResponse.getSubSchemes().add(resp);
			}
		} else if (subSchemeContractRequest.getSubScheme() != null) {
			SubScheme subSchemeEntity = new SubScheme(subSchemeContractRequest.getSubScheme());
			subSchemeJpaRepository.save(subSchemeEntity);
			SubSchemeContract resp = modelMapper.map(subSchemeEntity, SubSchemeContract.class);
			subSchemeContractResponse.setSubScheme(resp);
		}
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		return subSchemeContractResponse;
	}

	@Transactional
	public SubSchemeContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final SubSchemeContractRequest subSchemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SubSchemeUpdateAll"), SubSchemeContractRequest.class);
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (subSchemeContractRequest.getSubSchemes() != null && !subSchemeContractRequest.getSubSchemes().isEmpty()) {
			for (SubSchemeContract subSchemeContract : subSchemeContractRequest.getSubSchemes()) {
				SubScheme subSchemeEntity = new SubScheme(subSchemeContract);
				subSchemeEntity.setVersion(findOne(subSchemeEntity.getId()).getVersion());
				subSchemeJpaRepository.save(subSchemeEntity);
				SubSchemeContract resp = modelMapper.map(subSchemeEntity, SubSchemeContract.class);
				subSchemeContractResponse.getSubSchemes().add(resp);
			}
		}
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		return subSchemeContractResponse;
	}

	@Transactional
	public SubSchemeContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final SubSchemeContractRequest subSchemeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("SubSchemeUpdate"), SubSchemeContractRequest.class);
		SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		SubScheme subSchemeEntity = new SubScheme(subSchemeContractRequest.getSubScheme());
		subSchemeEntity.setVersion(subSchemeJpaRepository.findOne(subSchemeEntity.getId()).getVersion());
		subSchemeJpaRepository.save(subSchemeEntity);
		SubSchemeContract resp = modelMapper.map(subSchemeEntity, SubSchemeContract.class);
		subSchemeContractResponse.setSubScheme(resp);
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		return subSchemeContractResponse;
	}

	@Transactional
	public SubScheme create(final SubScheme subScheme) {
		setSubScheme(subScheme);
		return subSchemeJpaRepository.save(subScheme);
	}

	private void setSubScheme(final SubScheme subScheme) {
		if (subScheme.getScheme() != null) {
			Scheme scheme = schemeService.findOne(subScheme.getScheme());
			if (scheme == null) {
				throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
			}
			subScheme.setScheme(scheme.getId());
		}
	}

	@Transactional
	public SubScheme update(final SubScheme subScheme) {
		setSubScheme(subScheme);
		return subSchemeJpaRepository.save(subScheme);
	}

	public List<SubScheme> findAll() {
		return subSchemeJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public SubScheme findByName(String name) {
		return subSchemeJpaRepository.findByName(name);
	}

	public SubScheme findByCode(String code) {
		return subSchemeJpaRepository.findByCode(code);
	}

	public SubScheme findOne(Long id) {
		return subSchemeJpaRepository.findOne(id);
	}

	public Page<SubScheme> search(SubSchemeContractRequest subSchemeContractRequest) {
		final SubSchemeSpecification specification = new SubSchemeSpecification(
				subSchemeContractRequest.getSubScheme());
		Pageable page = new PageRequest(subSchemeContractRequest.getPage().getOffSet(),
				subSchemeContractRequest.getPage().getPageSize());
		return subSchemeJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(SubSchemeContractRequest subSchemeContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(subSchemeContractRequest.getSubScheme(), "SubScheme to edit must not be null");
				validator.validate(subSchemeContractRequest.getSubScheme(), errors);
				break;
			case "view":
				// validator.validate(subSchemeContractRequest.getSubScheme(),
				// errors);
				break;
			case "create":
				Assert.notNull(subSchemeContractRequest.getSubSchemes(), "SubSchemes to create must not be null");
				for (SubSchemeContract b : subSchemeContractRequest.getSubSchemes()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(subSchemeContractRequest.getSubSchemes(), "SubSchemes to create must not be null");
				for (SubSchemeContract b : subSchemeContractRequest.getSubSchemes()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(subSchemeContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public SubSchemeContractRequest fetchRelatedContracts(SubSchemeContractRequest subSchemeContractRequest) {
		ModelMapper model = new ModelMapper();
		for (SubSchemeContract subScheme : subSchemeContractRequest.getSubSchemes()) {
			if (subScheme.getScheme() != null) {
				Scheme scheme = schemeService.findOne(subScheme.getScheme().getId());
				if (scheme == null) {
					throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
				}
				model.map(scheme, subScheme.getScheme());
			}
		}
		SubSchemeContract subScheme = subSchemeContractRequest.getSubScheme();
		if (subScheme.getScheme() != null) {
			Scheme scheme = schemeService.findOne(subScheme.getScheme().getId());
			if (scheme == null) {
				throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
			}
			model.map(scheme, subScheme.getScheme());
		}
		return subSchemeContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}