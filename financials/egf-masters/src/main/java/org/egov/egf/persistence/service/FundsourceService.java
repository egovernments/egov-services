package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.egov.egf.persistence.queue.contract.FundsourceContractRequest;
import org.egov.egf.persistence.queue.contract.FundsourceContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FundSourceQueueRepository;
import org.egov.egf.persistence.repository.FundsourceJpaRepository;
import org.egov.egf.persistence.specification.FundsourceSpecification;
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
public class FundsourceService {

	private final FundsourceJpaRepository fundsourceJpaRepository;
	private final FundSourceQueueRepository fundSourceQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private FundsourceService fundsourceService;

	@Autowired
	public FundsourceService(final FundsourceJpaRepository fundsourceJpaRepository,
			final FundSourceQueueRepository fundSourceQueueRepository) {
		this.fundsourceJpaRepository = fundsourceJpaRepository;
		this.fundSourceQueueRepository = fundSourceQueueRepository;
	}

	public void push(final FundsourceContractRequest financialYearContractRequest) {
		fundSourceQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FundsourceContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FundsourceContractRequest fundsourceContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundSourceCreate"), FundsourceContractRequest.class);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fundsourceContractRequest.getFundsources() != null
				&& !fundsourceContractRequest.getFundsources().isEmpty()) {
			for (FundsourceContract fundsourceContract : fundsourceContractRequest.getFundsources()) {
				Fundsource fundsourceEntity = new Fundsource(fundsourceContract);
				fundsourceJpaRepository.save(fundsourceEntity);
				FundsourceContract resp = modelMapper.map(fundsourceEntity, FundsourceContract.class);
				fundsourceContractResponse.getFundsources().add(resp);
			}
		} else if (fundsourceContractRequest.getFundsource() != null) {
			Fundsource fundsourceEntity = new Fundsource(fundsourceContractRequest.getFundsource());
			fundsourceJpaRepository.save(fundsourceEntity);
			FundsourceContract resp = modelMapper.map(fundsourceEntity, FundsourceContract.class);
			fundsourceContractResponse.setFundsource(resp);
		}
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		return fundsourceContractResponse;
	}

	@Transactional
	public FundsourceContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FundsourceContractRequest fundsourceContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundSourceUpdateAll"), FundsourceContractRequest.class);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fundsourceContractRequest.getFundsources() != null
				&& !fundsourceContractRequest.getFundsources().isEmpty()) {
			for (FundsourceContract fundsourceContract : fundsourceContractRequest.getFundsources()) {
				Fundsource fundsourceEntity = new Fundsource(fundsourceContract);
				fundsourceEntity.setVersion(findOne(fundsourceEntity.getId()).getVersion());
				fundsourceJpaRepository.save(fundsourceEntity);
				FundsourceContract resp = modelMapper.map(fundsourceEntity, FundsourceContract.class);
				fundsourceContractResponse.getFundsources().add(resp);
			}
		}
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		return fundsourceContractResponse;
	}

	
	
	
	@Transactional
	public FundsourceContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FundsourceContractRequest fundsourceContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundSourceUpdate"), FundsourceContractRequest.class);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Fundsource fundsourceEntity = new Fundsource(fundsourceContractRequest.getFundsource());
		fundsourceEntity.setVersion(fundsourceJpaRepository.findOne(fundsourceEntity.getId()).getVersion());
		fundsourceJpaRepository.save(fundsourceEntity);
		FundsourceContract resp = modelMapper.map(fundsourceEntity, FundsourceContract.class);
		fundsourceContractResponse.setFundsource(resp);
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		return fundsourceContractResponse;
	}

	@Transactional
	public Fundsource create(final Fundsource fundsource) {
		setFundSource(fundsource);
		return fundsourceJpaRepository.save(fundsource);
	}

	private void setFundSource(final Fundsource fundsource) {
		if (fundsource.getFundSource() != null) {
			Fundsource fundSource = fundsourceService.findOne(fundsource.getFundSource());
			if (fundSource == null) {
				throw new InvalidDataException("fundSource", "fundSource.invalid", " Invalid fundSource");
			}
			fundsource.setFundSource(fundSource.getId());
		}
	}

	@Transactional
	public Fundsource update(final Fundsource fundsource) {
		setFundSource(fundsource);
		return fundsourceJpaRepository.save(fundsource);
	}

	public List<Fundsource> findAll() {
		return fundsourceJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Fundsource findByName(String name) {
		return fundsourceJpaRepository.findByName(name);
	}

	public Fundsource findByCode(String code) {
		return fundsourceJpaRepository.findByCode(code);
	}

	public Fundsource findOne(Long id) {
		return fundsourceJpaRepository.findOne(id);
	}

	public Page<Fundsource> search(FundsourceContractRequest fundsourceContractRequest) {
		final FundsourceSpecification specification = new FundsourceSpecification(
				fundsourceContractRequest.getFundsource());
		Pageable page = new PageRequest(fundsourceContractRequest.getPage().getOffSet(),
				fundsourceContractRequest.getPage().getPageSize());
		return fundsourceJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FundsourceContractRequest fundsourceContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(fundsourceContractRequest.getFundsource(), "Fundsource to edit must not be null");
				validator.validate(fundsourceContractRequest.getFundsource(), errors);
				break;
			case "view":
				// validator.validate(fundsourceContractRequest.getFundsource(),
				// errors);
				break;
			case "create":
				Assert.notNull(fundsourceContractRequest.getFundsources(), "Fundsources to create must not be null");
				for (FundsourceContract b : fundsourceContractRequest.getFundsources()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(fundsourceContractRequest.getFundsources(), "Fundsources to create must not be null");
				for (FundsourceContract b : fundsourceContractRequest.getFundsources()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(fundsourceContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FundsourceContractRequest fetchRelatedContracts(FundsourceContractRequest fundsourceContractRequest) {
		ModelMapper model = new ModelMapper();
		for (FundsourceContract fundsource : fundsourceContractRequest.getFundsources()) {
			if (fundsource.getFundSource() != null) {
				Fundsource fundSource = fundsourceService.findOne(fundsource.getFundSource().getId());
				if (fundSource == null) {
					throw new InvalidDataException("fundSource", "fundSource.invalid", " Invalid fundSource");
				}
				model.map(fundSource, fundsource.getFundSource());
			}
		}
		FundsourceContract fundsource = fundsourceContractRequest.getFundsource();
		if (fundsource.getFundSource() != null) {
			Fundsource fundSource = fundsourceService.findOne(fundsource.getFundSource().getId());
			if (fundSource == null) {
				throw new InvalidDataException("fundSource", "fundSource.invalid", " Invalid fundSource");
			}
			model.map(fundSource, fundsource.getFundSource());
		}
		return fundsourceContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}
