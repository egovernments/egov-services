package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractRequest;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.ChartOfAccountDetailJpaRepository;
import org.egov.egf.persistence.repository.ChartOfAccountDetailQueueRepository;
import org.egov.egf.persistence.specification.ChartOfAccountDetailSpecification;
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
public class ChartOfAccountDetailService {

	private final ChartOfAccountDetailJpaRepository chartOfAccountDetailJpaRepository;
	private final ChartOfAccountDetailQueueRepository chartOfAccountDetailQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private ChartOfAccountService chartOfAccountService;

	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

	@Autowired
	public ChartOfAccountDetailService(final ChartOfAccountDetailJpaRepository chartOfAccountDetailJpaRepository,
			final ChartOfAccountDetailQueueRepository chartOfAccountDetailQueueRepository) {
		this.chartOfAccountDetailJpaRepository = chartOfAccountDetailJpaRepository;
		this.chartOfAccountDetailQueueRepository = chartOfAccountDetailQueueRepository;
	}

	public void push(final ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest) {
		chartOfAccountDetailQueueRepository.push(chartOfAccountDetailContractRequest);
	}

	@Transactional
	public ChartOfAccountDetailContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("ChartOfAccountDetailCreate"),
						ChartOfAccountDetailContractRequest.class);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (chartOfAccountDetailContractRequest.getChartOfAccountDetails() != null
				&& !chartOfAccountDetailContractRequest.getChartOfAccountDetails().isEmpty()) {
			for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
					.getChartOfAccountDetails()) {
				ChartOfAccountDetail chartOfAccountDetailEntity = new ChartOfAccountDetail(
						chartOfAccountDetailContract);
				chartOfAccountDetailJpaRepository.save(chartOfAccountDetailEntity);
				ChartOfAccountDetailContract resp = modelMapper.map(chartOfAccountDetailEntity,
						ChartOfAccountDetailContract.class);
				chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(resp);
			}
		} else if (chartOfAccountDetailContractRequest.getChartOfAccountDetail() != null) {
			ChartOfAccountDetail chartOfAccountDetailEntity = new ChartOfAccountDetail(
					chartOfAccountDetailContractRequest.getChartOfAccountDetail());
			chartOfAccountDetailJpaRepository.save(chartOfAccountDetailEntity);
			ChartOfAccountDetailContract resp = modelMapper.map(chartOfAccountDetailEntity,
					ChartOfAccountDetailContract.class);
			chartOfAccountDetailContractResponse.setChartOfAccountDetail(resp);
		}
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		return chartOfAccountDetailContractResponse;
	}

	@Transactional
	public ChartOfAccountDetailContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("ChartOfAccountDetailUpdateAll"),
						ChartOfAccountDetailContractRequest.class);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (chartOfAccountDetailContractRequest.getChartOfAccountDetails() != null
				&& !chartOfAccountDetailContractRequest.getChartOfAccountDetails().isEmpty()) {
			for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
					.getChartOfAccountDetails()) {
				ChartOfAccountDetail chartOfAccountDetailEntity = new ChartOfAccountDetail(
						chartOfAccountDetailContract);
				chartOfAccountDetailEntity.setVersion(findOne(chartOfAccountDetailEntity.getId()).getVersion());
				chartOfAccountDetailJpaRepository.save(chartOfAccountDetailEntity);
				ChartOfAccountDetailContract resp = modelMapper.map(chartOfAccountDetailEntity,
						ChartOfAccountDetailContract.class);
				chartOfAccountDetailContractResponse.getChartOfAccountDetails().add(resp);
			}
		}
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		return chartOfAccountDetailContractResponse;
	}

	@Transactional
	public ChartOfAccountDetailContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("ChartOfAccountDetailUpdate"),
						ChartOfAccountDetailContractRequest.class);
		ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = new ChartOfAccountDetailContractResponse();
		chartOfAccountDetailContractResponse.setChartOfAccountDetails(new ArrayList<ChartOfAccountDetailContract>());
		ModelMapper modelMapper = new ModelMapper();
		ChartOfAccountDetail chartOfAccountDetailEntity = new ChartOfAccountDetail(
				chartOfAccountDetailContractRequest.getChartOfAccountDetail());
		chartOfAccountDetailEntity
				.setVersion(chartOfAccountDetailJpaRepository.findOne(chartOfAccountDetailEntity.getId()).getVersion());
		chartOfAccountDetailJpaRepository.save(chartOfAccountDetailEntity);
		ChartOfAccountDetailContract resp = modelMapper.map(chartOfAccountDetailEntity,
				ChartOfAccountDetailContract.class);
		chartOfAccountDetailContractResponse.setChartOfAccountDetail(resp);
		chartOfAccountDetailContractResponse
				.setResponseInfo(getResponseInfo(chartOfAccountDetailContractRequest.getRequestInfo()));
		return chartOfAccountDetailContractResponse;
	}

	@Transactional
	public ChartOfAccountDetail create(final ChartOfAccountDetail chartOfAccountDetail) {
		setChartOfAccountDetail(chartOfAccountDetail);
		return chartOfAccountDetailJpaRepository.save(chartOfAccountDetail);
	}

	private void setChartOfAccountDetail(final ChartOfAccountDetail chartOfAccountDetail) {
		if (chartOfAccountDetail.getChartOfAccount() != null) {
			ChartOfAccount chartOfAccount = chartOfAccountService.findOne(chartOfAccountDetail.getChartOfAccount());
			if (chartOfAccount == null) {
				throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
			}
			chartOfAccountDetail.setChartOfAccount(chartOfAccount.getId());
		}
		if (chartOfAccountDetail.getAccountDetailType() != null) {
			AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(chartOfAccountDetail.getAccountDetailType());
			if (accountDetailType == null) {
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			}
			chartOfAccountDetail.setAccountDetailType(accountDetailType.getId());
		}
	}

	@Transactional
	public ChartOfAccountDetail update(final ChartOfAccountDetail chartOfAccountDetail) {
		setChartOfAccountDetail(chartOfAccountDetail);
		return chartOfAccountDetailJpaRepository.save(chartOfAccountDetail);
	}

	public List<ChartOfAccountDetail> findAll() {
		return chartOfAccountDetailJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public ChartOfAccountDetail findOne(Long id) {
		return chartOfAccountDetailJpaRepository.findOne(id);
	}

	public Page<ChartOfAccountDetail> search(ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest) {
		final ChartOfAccountDetailSpecification specification = new ChartOfAccountDetailSpecification(
				chartOfAccountDetailContractRequest.getChartOfAccountDetail());
		Pageable page = new PageRequest(chartOfAccountDetailContractRequest.getPage().getOffSet(),
				chartOfAccountDetailContractRequest.getPage().getPageSize());
		return chartOfAccountDetailJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest,
			String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetail(),
						"ChartOfAccountDetail to edit must not be null");
				validator.validate(chartOfAccountDetailContractRequest.getChartOfAccountDetail(), errors);
				break;
			case "view":
				// validator.validate(chartOfAccountDetailContractRequest.getChartOfAccountDetail(),
				// errors);
				break;
			case "create":
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetails(),
						"ChartOfAccountDetails to create must not be null");
				for (ChartOfAccountDetailContract b : chartOfAccountDetailContractRequest.getChartOfAccountDetails()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(chartOfAccountDetailContractRequest.getChartOfAccountDetails(),
						"ChartOfAccountDetails to create must not be null");
				for (ChartOfAccountDetailContract b : chartOfAccountDetailContractRequest.getChartOfAccountDetails()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(chartOfAccountDetailContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public ChartOfAccountDetailContractRequest fetchRelatedContracts(
			ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest) {
		ModelMapper model = new ModelMapper();
		for (ChartOfAccountDetailContract chartOfAccountDetail : chartOfAccountDetailContractRequest
				.getChartOfAccountDetails()) {
			if (chartOfAccountDetail.getChartOfAccount() != null) {
				ChartOfAccount chartOfAccount = chartOfAccountService
						.findOne(chartOfAccountDetail.getChartOfAccount().getId());
				if (chartOfAccount == null) {
					throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
							" Invalid chartOfAccount");
				}
				model.map(chartOfAccount, chartOfAccountDetail.getChartOfAccount());
			}
			if (chartOfAccountDetail.getAccountDetailType() != null) {
				AccountDetailType accountDetailType = accountDetailTypeService
						.findOne(chartOfAccountDetail.getAccountDetailType().getId());
				if (accountDetailType == null) {
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				}
				model.map(accountDetailType, chartOfAccountDetail.getAccountDetailType());
			}
		}
		ChartOfAccountDetailContract chartOfAccountDetail = chartOfAccountDetailContractRequest
				.getChartOfAccountDetail();
		if (chartOfAccountDetail.getChartOfAccount() != null) {
			ChartOfAccount chartOfAccount = chartOfAccountService
					.findOne(chartOfAccountDetail.getChartOfAccount().getId());
			if (chartOfAccount == null) {
				throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
			}
			model.map(chartOfAccount, chartOfAccountDetail.getChartOfAccount());
		}
		if (chartOfAccountDetail.getAccountDetailType() != null) {
			AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(chartOfAccountDetail.getAccountDetailType().getId());
			if (accountDetailType == null) {
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			}
			model.map(accountDetailType, chartOfAccountDetail.getAccountDetailType());
		}
		return chartOfAccountDetailContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}