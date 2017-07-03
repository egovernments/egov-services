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
import org.egov.egf.persistence.entity.FundSearchCriteria;
import org.egov.egf.persistence.queue.contract.FundContract;
import org.egov.egf.persistence.queue.contract.FundContractRequest;
import org.egov.egf.persistence.queue.contract.FundContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.FundESRepository;
import org.egov.egf.persistence.repository.FundJpaRepository;
import org.egov.egf.persistence.repository.FundQueueRepository;
import org.egov.egf.persistence.specification.FundSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class FundService {

	private final FundJpaRepository fundJpaRepository;
	private final FundQueueRepository fundQueueRepository;
	private final FundESRepository fundESRepository;
	private final String fetchDataFrom;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private FundService fundService;

	@Autowired
	public FundService(final FundJpaRepository fundJpaRepository, final FundQueueRepository fundQueueRepository,
			final FundESRepository fundESRepository, @Value("${fetch_data_from}") String fetchDataFrom) {
		this.fundJpaRepository = fundJpaRepository;
		this.fundQueueRepository = fundQueueRepository;
		this.fundESRepository = fundESRepository;
		this.fetchDataFrom = fetchDataFrom;
	}

	public void push(final FundContractRequest financialYearContractRequest) {
		fundQueueRepository.push(financialYearContractRequest);
	}

	@Transactional
	public FundContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final FundContractRequest fundContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundCreate"), FundContractRequest.class);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFunds(new ArrayList<FundContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fundContractRequest.getFunds() != null && !fundContractRequest.getFunds().isEmpty()) {
			for (FundContract fundContract : fundContractRequest.getFunds()) {
				Fund fundEntity = new Fund(fundContract);
				fundJpaRepository.save(fundEntity);
				FundContract resp = modelMapper.map(fundEntity, FundContract.class);
				fundContractResponse.getFunds().add(resp);
			}
		} else if (fundContractRequest.getFund() != null) {
			Fund fundEntity = new Fund(fundContractRequest.getFund());
			fundJpaRepository.save(fundEntity);
			FundContract resp = modelMapper.map(fundEntity, FundContract.class);
			fundContractResponse.setFund(resp);
		}
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		return fundContractResponse;
	}

	@Transactional
	public FundContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final FundContractRequest fundContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundUpdateAll"), FundContractRequest.class);
		FundContractResponse fundContractResponse = new FundContractResponse();
		fundContractResponse.setFunds(new ArrayList<FundContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (fundContractRequest.getFunds() != null && !fundContractRequest.getFunds().isEmpty()) {
			for (FundContract fundContract : fundContractRequest.getFunds()) {
				Fund fundEntity = new Fund(fundContract);
				fundEntity.setVersion(findOne(fundEntity.getId()).getVersion());
				fundJpaRepository.save(fundEntity);
				FundContract resp = modelMapper.map(fundEntity, FundContract.class);
				fundContractResponse.getFunds().add(resp);
			}
		}
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		return fundContractResponse;
	}

	@Transactional
	public FundContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final FundContractRequest fundContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("FundUpdate"), FundContractRequest.class);
		FundContractResponse fundContractResponse = new FundContractResponse();
		ModelMapper modelMapper = new ModelMapper();
		Fund fundEntity = new Fund(fundContractRequest.getFund());
		fundEntity.setVersion(fundJpaRepository.findOne(fundEntity.getId()).getVersion());
		fundJpaRepository.save(fundEntity);
		FundContract resp = modelMapper.map(fundEntity, FundContract.class);
		fundContractResponse.setFund(resp);
		fundContractResponse.setResponseInfo(getResponseInfo(fundContractRequest.getRequestInfo()));
		return fundContractResponse;
	}

	@Transactional
	public Fund create(final Fund fund) {
		setFund(fund);
		return fundJpaRepository.save(fund);
	}

	private void setFund(final Fund fund) {
		if (fund.getParentId() != null) {
			Fund parentId = fundService.findOne(fund.getParentId());
			if (parentId == null) {
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			}
			fund.setParentId(parentId.getId());
		}
	}

	@Transactional
	public Fund update(final Fund fund) {
		setFund(fund);
		return fundJpaRepository.save(fund);
	}

	public List<Fund> findAll() {
		return fundJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Fund findByName(String name) {
		return fundJpaRepository.findByName(name);
	}

	public Fund findByCode(String code) {
		return fundJpaRepository.findByCode(code);
	}

	public Fund findOne(Long id) {
		return fundJpaRepository.findOne(id);
	}

	public Page<Fund> search(FundContractRequest fundContractRequest) {
		if (fetchDataFrom != null && !fetchDataFrom.isEmpty() && fetchDataFrom.equalsIgnoreCase("es")) {
			FundSearchCriteria fundSearchCriteria = new FundSearchCriteria();
			List<Fund> funds = fundESRepository.getMatchingFunds(fundSearchCriteria);
			PageRequest pageable = new PageRequest(fundContractRequest.getPage().getCurrentPage(),
					fundContractRequest.getPage().getPageSize());
			int start = fundContractRequest.getPage().getOffSet();
			int end = (start + pageable.getPageSize()) > funds.size() ? funds.size() : (start + pageable.getPageSize());
			return new PageImpl<Fund>(funds.subList(start, end), pageable, funds.size());
		}
		final FundSpecification specification = new FundSpecification(fundContractRequest.getFund());
		Pageable page = new PageRequest(fundContractRequest.getPage().getOffSet(),
				fundContractRequest.getPage().getPageSize());
		return fundJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(FundContractRequest fundContractRequest, String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(fundContractRequest.getFund(), "Fund to edit must not be null");
				validator.validate(fundContractRequest.getFund(), errors);
				break;
			case "view":
				// validator.validate(fundContractRequest.getFund(), errors);
				break;
			case "create":
				Assert.notNull(fundContractRequest.getFunds(), "Funds to create must not be null");
				for (FundContract b : fundContractRequest.getFunds()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(fundContractRequest.getFunds(), "Funds to create must not be null");
				for (FundContract b : fundContractRequest.getFunds()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(fundContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public FundContractRequest fetchRelatedContracts(FundContractRequest fundContractRequest) {
		ModelMapper model = new ModelMapper();
		for (FundContract fund : fundContractRequest.getFunds()) {
			if (fund.getParentId() != null) {
				Fund parentId = fundService.findOne(fund.getParentId().getId());
				if (parentId == null) {
					throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
				}
				model.map(parentId, fund.getParentId());
			}
		}
		FundContract fund = fundContractRequest.getFund();
		if (fund.getParentId() != null) {
			Fund parentId = fundService.findOne(fund.getParentId().getId());
			if (parentId == null) {
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			}
			model.map(parentId, fund.getParentId());
		}
		return fundContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}