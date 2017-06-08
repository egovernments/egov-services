package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractRequest;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FiscalPeriodJpaRepository;
import org.egov.egf.persistence.repository.FiscalPeriodQueueRepository;
import org.egov.egf.persistence.specification.FiscalPeriodSpecification;
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
public class FiscalPeriodService {

	private final FiscalPeriodJpaRepository fiscalPeriodJpaRepository;
	private final FiscalPeriodQueueRepository fiscalPeriodQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private FinancialYearService financialYearService;

	public void push(final FiscalPeriodContractRequest financialYearContractRequest) {
		fiscalPeriodQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FiscalPeriodContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FiscalPeriodContractRequest fiscalPeriodContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FiscalPeriodCreate"), FiscalPeriodContractRequest.class);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fiscalPeriodContractRequest.getFiscalPeriods() != null
				&& !fiscalPeriodContractRequest.getFiscalPeriods().isEmpty()) {
			for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getFiscalPeriods()) {
				FiscalPeriod fiscalPeriodEntity = new FiscalPeriod(fiscalPeriodContract);
				fiscalPeriodJpaRepository.save(fiscalPeriodEntity);
				FiscalPeriodContract resp = modelMapper.map(fiscalPeriodEntity, FiscalPeriodContract.class);
				fiscalPeriodContractResponse.getFiscalPeriods().add(resp);
			}
		} else if (fiscalPeriodContractRequest.getFiscalPeriod() != null) {
			FiscalPeriod fiscalPeriodEntity = new FiscalPeriod(fiscalPeriodContractRequest.getFiscalPeriod());
			fiscalPeriodJpaRepository.save(fiscalPeriodEntity);
			FiscalPeriodContract resp = modelMapper.map(fiscalPeriodEntity, FiscalPeriodContract.class);
			fiscalPeriodContractResponse.setFiscalPeriod(resp);
		}
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		return fiscalPeriodContractResponse;
	}
	
	@Transactional
	public FiscalPeriodContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FiscalPeriodContractRequest fiscalPeriodContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FiscalPeriodUpdateAll"), FiscalPeriodContractRequest.class);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fiscalPeriodContractRequest.getFiscalPeriods() != null
				&& !fiscalPeriodContractRequest.getFiscalPeriods().isEmpty()) {
			for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractRequest.getFiscalPeriods()) {
				FiscalPeriod fiscalPeriodEntity = new FiscalPeriod(fiscalPeriodContract);
				fiscalPeriodEntity.setVersion(findOne(fiscalPeriodEntity.getId()).getVersion());
				fiscalPeriodJpaRepository.save(fiscalPeriodEntity);
				FiscalPeriodContract resp = modelMapper.map(fiscalPeriodEntity, FiscalPeriodContract.class);
				fiscalPeriodContractResponse.getFiscalPeriods().add(resp);
			}
		}
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		return fiscalPeriodContractResponse;
	}

	
	
	@Transactional
	public FiscalPeriodContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FiscalPeriodContractRequest fiscalPeriodContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FiscalPeriodUpdate"), FiscalPeriodContractRequest.class);
		FiscalPeriodContractResponse fiscalPeriodContractResponse = new FiscalPeriodContractResponse();
		fiscalPeriodContractResponse.setFiscalPeriods(new ArrayList<FiscalPeriodContract>());
		ModelMapper modelMapper = new ModelMapper();
		FiscalPeriod fiscalPeriodEntity = new FiscalPeriod(fiscalPeriodContractRequest.getFiscalPeriod());
		fiscalPeriodEntity.setVersion(fiscalPeriodJpaRepository.findOne(fiscalPeriodEntity.getId()).getVersion());
		fiscalPeriodJpaRepository.save(fiscalPeriodEntity);
		FiscalPeriodContract resp = modelMapper.map(fiscalPeriodEntity, FiscalPeriodContract.class);
		fiscalPeriodContractResponse.setFiscalPeriod(resp);
		fiscalPeriodContractResponse.setResponseInfo(getResponseInfo(fiscalPeriodContractRequest.getRequestInfo()));
		return fiscalPeriodContractResponse;
	}

	@Autowired
	public FiscalPeriodService(final FiscalPeriodJpaRepository fiscalPeriodJpaRepository,
			final FiscalPeriodQueueRepository fiscalPeriodQueueRepository) {
		this.fiscalPeriodJpaRepository = fiscalPeriodJpaRepository;
		this.fiscalPeriodQueueRepository = fiscalPeriodQueueRepository;
	}

	@Transactional
	public FiscalPeriod create(final FiscalPeriod fiscalPeriod) {
		setFiscalPeriod(fiscalPeriod);
		return fiscalPeriodJpaRepository.save(fiscalPeriod);
	}

	private void setFiscalPeriod(final FiscalPeriod fiscalPeriod) {
		if (fiscalPeriod.getFinancialYear() != null) {
			FinancialYear financialYear = financialYearService.findOne(fiscalPeriod.getFinancialYear());
			if (financialYear == null) {
				throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
			}
			fiscalPeriod.setFinancialYear(financialYear.getId());
		}
	}

	@Transactional
	public FiscalPeriod update(final FiscalPeriod fiscalPeriod) {
		setFiscalPeriod(fiscalPeriod);
		return fiscalPeriodJpaRepository.save(fiscalPeriod);
	}

	public List<FiscalPeriod> findAll() {
		return fiscalPeriodJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public FiscalPeriod findByName(String name) {
		return fiscalPeriodJpaRepository.findByName(name);
	}

	public FiscalPeriod findOne(Long id) {
		return fiscalPeriodJpaRepository.findOne(id);
	}

	public Page<FiscalPeriod> search(FiscalPeriodContractRequest fiscalPeriodContractRequest) {
		final FiscalPeriodSpecification specification = new FiscalPeriodSpecification(
				fiscalPeriodContractRequest.getFiscalPeriod());
		Pageable page = new PageRequest(fiscalPeriodContractRequest.getPage().getOffSet(),
				fiscalPeriodContractRequest.getPage().getPageSize());
		return fiscalPeriodJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FiscalPeriodContractRequest fiscalPeriodContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriod(), "FiscalPeriod to edit must not be null");
				validator.validate(fiscalPeriodContractRequest.getFiscalPeriod(), errors);
				break;
			case "view":
				// validator.validate(fiscalPeriodContractRequest.getFiscalPeriod(),
				// errors);
				break;
			case "create":
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriods(),
						"FiscalPeriods to create must not be null");
				for (FiscalPeriodContract b : fiscalPeriodContractRequest.getFiscalPeriods()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriods(),
						"FiscalPeriods to create must not be null");
				for (FiscalPeriodContract b : fiscalPeriodContractRequest.getFiscalPeriods()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(fiscalPeriodContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FiscalPeriodContractRequest fetchRelatedContracts(FiscalPeriodContractRequest fiscalPeriodContractRequest) {
		ModelMapper model = new ModelMapper();
		for (FiscalPeriodContract fiscalPeriod : fiscalPeriodContractRequest.getFiscalPeriods()) {
			if (fiscalPeriod.getFinancialYear() != null) {
				FinancialYear financialYear = financialYearService.findOne(fiscalPeriod.getFinancialYear().getId());
				if (financialYear == null) {
					throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
				}
				model.map(financialYear, fiscalPeriod.getFinancialYear());
			}
		}
		FiscalPeriodContract fiscalPeriod = fiscalPeriodContractRequest.getFiscalPeriod();
		if (fiscalPeriod.getFinancialYear() != null) {
			FinancialYear financialYear = financialYearService.findOne(fiscalPeriod.getFinancialYear().getId());
			if (financialYear == null) {
				throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
			}
			model.map(financialYear, fiscalPeriod.getFinancialYear());
		}
		return fiscalPeriodContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}
