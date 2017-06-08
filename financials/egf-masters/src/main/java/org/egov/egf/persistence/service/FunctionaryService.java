package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.queue.contract.FunctionaryContract;
import org.egov.egf.persistence.queue.contract.FunctionaryContractRequest;
import org.egov.egf.persistence.queue.contract.FunctionaryContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FunctionaryJpaRepository;
import org.egov.egf.persistence.repository.FunctionaryQueueRepository;
import org.egov.egf.persistence.specification.FunctionarySpecification;
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
public class FunctionaryService {

	private final FunctionaryJpaRepository functionaryJpaRepository;
	private final FunctionaryQueueRepository functionaryQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public FunctionaryService(final FunctionaryJpaRepository functionaryJpaRepository,
			final FunctionaryQueueRepository functionaryQueueRepository) {
		this.functionaryJpaRepository = functionaryJpaRepository;
		this.functionaryQueueRepository = functionaryQueueRepository;
	}

	public void push(final FunctionaryContractRequest financialYearContractRequest) {
		functionaryQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FunctionaryContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FunctionaryContractRequest functionaryContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionaryCreate"), FunctionaryContractRequest.class);
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		functionaryContractResponse.setFunctionaries(new ArrayList<FunctionaryContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (functionaryContractRequest.getFunctionaries() != null
				&& !functionaryContractRequest.getFunctionaries().isEmpty()) {
			for (FunctionaryContract functionaryContract : functionaryContractRequest.getFunctionaries()) {
				Functionary functionaryEntity = new Functionary(functionaryContract);
				functionaryJpaRepository.save(functionaryEntity);
				FunctionaryContract resp = modelMapper.map(functionaryEntity, FunctionaryContract.class);
				functionaryContractResponse.getFunctionaries().add(resp);
			}
		} else if (functionaryContractRequest.getFunctionary() != null) {
			Functionary functionaryEntity = new Functionary(functionaryContractRequest.getFunctionary());
			functionaryJpaRepository.save(functionaryEntity);
			FunctionaryContract resp = modelMapper.map(functionaryEntity, FunctionaryContract.class);
			functionaryContractResponse.setFunctionary(resp);
		}
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		return functionaryContractResponse;
	}
	
	@Transactional
	public FunctionaryContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FunctionaryContractRequest functionaryContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionaryUpdateAll"), FunctionaryContractRequest.class);
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		functionaryContractResponse.setFunctionaries(new ArrayList<FunctionaryContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (functionaryContractRequest.getFunctionaries() != null
				&& !functionaryContractRequest.getFunctionaries().isEmpty()) {
			for (FunctionaryContract functionaryContract : functionaryContractRequest.getFunctionaries()) {
				Functionary functionaryEntity = new Functionary(functionaryContract);
				functionaryEntity.setVersion(findOne(functionaryEntity.getId()).getVersion());
				functionaryJpaRepository.save(functionaryEntity);
				FunctionaryContract resp = modelMapper.map(functionaryEntity, FunctionaryContract.class);
				functionaryContractResponse.getFunctionaries().add(resp);
			}
		}
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		return functionaryContractResponse;
	}
	
	

	@Transactional
	public FunctionaryContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FunctionaryContractRequest functionaryContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionaryUpdate"), FunctionaryContractRequest.class);
		FunctionaryContractResponse functionaryContractResponse = new FunctionaryContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Functionary functionaryEntity = new Functionary(functionaryContractRequest.getFunctionary());
		functionaryEntity.setVersion(functionaryJpaRepository.findOne(functionaryEntity.getId()).getVersion());
		functionaryJpaRepository.save(functionaryEntity);
		FunctionaryContract resp = modelMapper.map(functionaryEntity, FunctionaryContract.class);
		functionaryContractResponse.setFunctionary(resp);
		functionaryContractResponse.setResponseInfo(getResponseInfo(functionaryContractRequest.getRequestInfo()));
		return functionaryContractResponse;
	}

	@Transactional
	public Functionary create(final Functionary functionary) {
		return functionaryJpaRepository.save(functionary);
	}

	@Transactional
	public Functionary update(final Functionary functionary) {
		return functionaryJpaRepository.save(functionary);
	}

	public List<Functionary> findAll() {
		return functionaryJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Functionary findByName(String name) {
		return functionaryJpaRepository.findByName(name);
	}

	public Functionary findByCode(String code) {
		return functionaryJpaRepository.findByCode(code);
	}

	public Functionary findOne(Long id) {
		return functionaryJpaRepository.findOne(id);
	}

	public Page<Functionary> search(FunctionaryContractRequest functionaryContractRequest) {
		final FunctionarySpecification specification = new FunctionarySpecification(
				functionaryContractRequest.getFunctionary());
		Pageable page = new PageRequest(functionaryContractRequest.getPage().getOffSet(),
				functionaryContractRequest.getPage().getPageSize());
		return functionaryJpaRepository.findAll(specification, page);
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

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}