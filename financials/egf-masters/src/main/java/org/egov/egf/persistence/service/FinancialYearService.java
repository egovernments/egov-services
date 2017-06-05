package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.queue.contract.FinancialYearContract;
import org.egov.egf.persistence.queue.contract.FinancialYearContractRequest;
import org.egov.egf.persistence.queue.contract.FinancialYearContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FinancialYearJpaRepository;
import org.egov.egf.persistence.repository.FinancialYearQueueRepository;
import org.egov.egf.persistence.specification.FinancialYearSpecification;
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
public class FinancialYearService {

	private final FinancialYearJpaRepository financialYearJpaRepository;
	private final FinancialYearQueueRepository financialYearQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public FinancialYearService(final FinancialYearJpaRepository financialYearJpaRepository,
			final FinancialYearQueueRepository financialYearQueueRepository) {
		this.financialYearJpaRepository = financialYearJpaRepository;
		this.financialYearQueueRepository = financialYearQueueRepository;
	}

	public void push(final FinancialYearContractRequest financialYearContractRequest) {
		financialYearQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FinancialYearContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FinancialYearContractRequest financialYearContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("FinancialYearCreate"), FinancialYearContractRequest.class);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (financialYearContractRequest.getFinancialYears() != null
				&& !financialYearContractRequest.getFinancialYears().isEmpty()) {
			for (FinancialYearContract financialYearContract : financialYearContractRequest.getFinancialYears()) {
				FinancialYear financialYearEntity = new FinancialYear(financialYearContract);
				financialYearJpaRepository.save(financialYearEntity);
				FinancialYearContract resp = modelMapper.map(financialYearEntity, FinancialYearContract.class);
				financialYearContractResponse.getFinancialYears().add(resp);
			}
		} else if (financialYearContractRequest.getFinancialYear() != null) {
			FinancialYear financialYearEntity = new FinancialYear(financialYearContractRequest.getFinancialYear());
			financialYearJpaRepository.save(financialYearEntity);
			FinancialYearContract resp = modelMapper.map(financialYearEntity, FinancialYearContract.class);
			financialYearContractResponse.setFinancialYear(resp);
		}
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		return financialYearContractResponse;
	}

	@Transactional
	public FinancialYearContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FinancialYearContractRequest financialYearContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("FinancialYearUpdateAll"), FinancialYearContractRequest.class);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (financialYearContractRequest.getFinancialYears() != null
				&& !financialYearContractRequest.getFinancialYears().isEmpty()) {
			for (FinancialYearContract financialYearContract : financialYearContractRequest.getFinancialYears()) {
				FinancialYear financialYearEntity = new FinancialYear(financialYearContract);
				financialYearEntity.setVersion(findOne(financialYearEntity.getId()).getVersion());
				financialYearJpaRepository.save(financialYearEntity);
				FinancialYearContract resp = modelMapper.map(financialYearEntity, FinancialYearContract.class);
				financialYearContractResponse.getFinancialYears().add(resp);
			}
		}
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		return financialYearContractResponse;
	}

	@Transactional
	public FinancialYearContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FinancialYearContractRequest financialYearContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("FinancialYearUpdate"), FinancialYearContractRequest.class);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		ModelMapper modelMapper = new ModelMapper();
		FinancialYear financialYearEntity = new FinancialYear(financialYearContractRequest.getFinancialYear());
		financialYearEntity.setVersion(financialYearJpaRepository.findOne(financialYearEntity.getId()).getVersion());
		financialYearJpaRepository.save(financialYearEntity);
		FinancialYearContract resp = modelMapper.map(financialYearEntity, FinancialYearContract.class);
		financialYearContractResponse.setFinancialYear(resp);
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		return financialYearContractResponse;
	}

	@Transactional
	public FinancialYear create(final FinancialYear financialYear) {
		return financialYearJpaRepository.save(financialYear);
	}

	@Transactional
	public FinancialYear update(final FinancialYear financialYear) {
		return financialYearJpaRepository.save(financialYear);
	}

	public List<FinancialYear> findAll() {
		return financialYearJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FinancialYear findOne(Long id) {
		return financialYearJpaRepository.findOne(id);
	}

	public Page<FinancialYear> search(FinancialYearContractRequest financialYearContractRequest) {
		final FinancialYearSpecification specification = new FinancialYearSpecification(
				financialYearContractRequest.getFinancialYear());
		Pageable page = new PageRequest(financialYearContractRequest.getPage().getOffSet(),
				financialYearContractRequest.getPage().getPageSize());
		return financialYearJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FinancialYearContractRequest financialYearContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(financialYearContractRequest.getFinancialYear(),
						"FinancialYear to edit must not be null");
				validator.validate(financialYearContractRequest.getFinancialYear(), errors);
				break;
			case "view":
				// validator.validate(financialYearContractRequest.getFinancialYear(),
				// errors);
				break;
			case "create":
				Assert.notNull(financialYearContractRequest.getFinancialYears(),
						"FinancialYears to create must not be null");
				for (FinancialYearContract b : financialYearContractRequest.getFinancialYears()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(financialYearContractRequest.getFinancialYears(),
						"FinancialYears to create must not be null");
				for (FinancialYearContract b : financialYearContractRequest.getFinancialYears()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(financialYearContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FinancialYearContractRequest fetchRelatedContracts(
			FinancialYearContractRequest financialYearContractRequest) {
		return financialYearContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}