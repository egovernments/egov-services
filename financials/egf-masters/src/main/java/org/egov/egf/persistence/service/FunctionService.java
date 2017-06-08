package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.egov.egf.persistence.queue.contract.FunctionContractRequest;
import org.egov.egf.persistence.queue.contract.FunctionContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FunctionJpaRepository;
import org.egov.egf.persistence.repository.FunctionQueueRepository;
import org.egov.egf.persistence.specification.FunctionSpecification;
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
public class FunctionService {

	private final FunctionJpaRepository functionJpaRepository;
	private final FunctionQueueRepository functionQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private FunctionService functionService;

	@Autowired
	public FunctionService(final FunctionJpaRepository functionJpaRepository,
			final FunctionQueueRepository functionQueueRepository) {
		this.functionJpaRepository = functionJpaRepository;
		this.functionQueueRepository = functionQueueRepository;
	}

	public void push(final FunctionContractRequest financialYearContractRequest) {
		functionQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FunctionContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FunctionContractRequest functionContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionCreate"), FunctionContractRequest.class);
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (functionContractRequest.getFunctions() != null && !functionContractRequest.getFunctions().isEmpty()) {
			for (FunctionContract functionContract : functionContractRequest.getFunctions()) {
				Function functionEntity = new Function(functionContract);
				functionJpaRepository.save(functionEntity);
				FunctionContract resp = modelMapper.map(functionEntity, FunctionContract.class);
				functionContractResponse.getFunctions().add(resp);
			}
		} else if (functionContractRequest.getFunction() != null) {
			Function functionEntity = new Function(functionContractRequest.getFunction());
			functionJpaRepository.save(functionEntity);
			FunctionContract resp = modelMapper.map(functionEntity, FunctionContract.class);
			functionContractResponse.setFunction(resp);
		}
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		return functionContractResponse;
	}
	
	@Transactional
	public FunctionContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FunctionContractRequest functionContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionUpdateAll"), FunctionContractRequest.class);
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		functionContractResponse.setFunctions(new ArrayList<FunctionContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (functionContractRequest.getFunctions() != null && !functionContractRequest.getFunctions().isEmpty()) {
			for (FunctionContract functionContract : functionContractRequest.getFunctions()) {
				Function functionEntity = new Function(functionContract);
				functionEntity.setVersion(findOne(functionEntity.getId()).getVersion());
				functionJpaRepository.save(functionEntity);
				FunctionContract resp = modelMapper.map(functionEntity, FunctionContract.class);
				functionContractResponse.getFunctions().add(resp);
			}
		}
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		return functionContractResponse;
	}

	
	
	@Transactional
	public FunctionContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FunctionContractRequest functionContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FunctionUpdate"), FunctionContractRequest.class);
		FunctionContractResponse functionContractResponse = new FunctionContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Function functionEntity = new Function(functionContractRequest.getFunction());
		functionEntity.setVersion(functionJpaRepository.findOne(functionEntity.getId()).getVersion());
		functionJpaRepository.save(functionEntity);
		FunctionContract resp = modelMapper.map(functionEntity, FunctionContract.class);
		functionContractResponse.setFunction(resp);
		functionContractResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		return functionContractResponse;
	}

	@Transactional
	public Function create(final Function function) {
		setFunction(function);
		return functionJpaRepository.save(function);
	}

	private void setFunction(final Function function) {
		if (function.getParentId() != null) {
			Function parentId = functionService.findOne(function.getParentId());
			if (parentId == null) {
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			}
			function.setParentId(parentId.getId());
		}
	}

	@Transactional
	public Function update(final Function function) {
		setFunction(function);
		return functionJpaRepository.save(function);
	}

	public List<Function> findAll() {
		return functionJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Function findByName(String name) {
		return functionJpaRepository.findByName(name);
	}

	public Function findByCode(String code) {
		return functionJpaRepository.findByCode(code);
	}

	public Function findOne(Long id) {
		return functionJpaRepository.findOne(id);
	}

	public Page<Function> search(FunctionContractRequest functionContractRequest) {
		final FunctionSpecification specification = new FunctionSpecification(functionContractRequest.getFunction());
		Pageable page = new PageRequest(functionContractRequest.getPage().getOffSet(),
				functionContractRequest.getPage().getPageSize());
		return functionJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FunctionContractRequest functionContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(functionContractRequest.getFunction(), "Function to edit must not be null");
				validator.validate(functionContractRequest.getFunction(), errors);
				break;
			case "view":
				// validator.validate(functionContractRequest.getFunction(),
				// errors);
				break;
			case "create":
				Assert.notNull(functionContractRequest.getFunctions(), "Functions to create must not be null");
				for (FunctionContract b : functionContractRequest.getFunctions()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(functionContractRequest.getFunctions(), "Functions to create must not be null");
				for (FunctionContract b : functionContractRequest.getFunctions()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(functionContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FunctionContractRequest fetchRelatedContracts(FunctionContractRequest functionContractRequest) {
		ModelMapper model = new ModelMapper();
		for (FunctionContract function : functionContractRequest.getFunctions()) {
			if (function.getParentId() != null) {
				Function parentId = functionService.findOne(function.getParentId().getId());
				if (parentId == null) {
					throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
				}
				model.map(parentId, function.getParentId());
			}
		}
		FunctionContract function = functionContractRequest.getFunction();
		if (function.getParentId() != null) {
			Function parentId = functionService.findOne(function.getParentId().getId());
			if (parentId == null) {
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			}
			model.map(parentId, function.getParentId());
		}
		return functionContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}