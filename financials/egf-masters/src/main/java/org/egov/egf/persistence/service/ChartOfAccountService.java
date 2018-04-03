package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractRequest;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.ChartOfAccountJpaRepository;
import org.egov.egf.persistence.repository.ChartOfAccountQueueRepository;
import org.egov.egf.persistence.specification.ChartOfAccountSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChartOfAccountService {

	private final ChartOfAccountJpaRepository chartOfAccountJpaRepository;
	private final ChartOfAccountQueueRepository chartOfAccountQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private ChartOfAccountService chartOfAccountService;

	@Autowired
	private AccountCodePurposeService accountCodePurposeService;

	@Autowired
	public ChartOfAccountService(final ChartOfAccountJpaRepository chartOfAccountJpaRepository,
			final ChartOfAccountQueueRepository chartOfAccountQueueRepository) {
		this.chartOfAccountJpaRepository = chartOfAccountJpaRepository;
		this.chartOfAccountQueueRepository = chartOfAccountQueueRepository;
	}

	public void push(final ChartOfAccountContractRequest chartOfAccountContractRequest) {
		chartOfAccountQueueRepository.push(chartOfAccountContractRequest);
	}

	@Transactional
	public ChartOfAccountContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountContractRequest chartOfAccountContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("ChartOfAccountCreate"), ChartOfAccountContractRequest.class);
		ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
		chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (chartOfAccountContractRequest.getChartOfAccounts() != null
				&& !chartOfAccountContractRequest.getChartOfAccounts().isEmpty()) {
			for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getChartOfAccounts()) {
				ChartOfAccount chartOfAccountEntity = new ChartOfAccount(chartOfAccountContract);
				chartOfAccountJpaRepository.save(chartOfAccountEntity);
				ChartOfAccountContract resp = modelMapper.map(chartOfAccountEntity, ChartOfAccountContract.class);
				chartOfAccountContractResponse.getChartOfAccounts().add(resp);
			}
		} else if (chartOfAccountContractRequest.getChartOfAccount() != null) {
			ChartOfAccount chartOfAccountEntity = new ChartOfAccount(chartOfAccountContractRequest.getChartOfAccount());
			chartOfAccountJpaRepository.save(chartOfAccountEntity);
			ChartOfAccountContract resp = modelMapper.map(chartOfAccountEntity, ChartOfAccountContract.class);
			chartOfAccountContractResponse.setChartOfAccount(resp);
		}
		chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
		return chartOfAccountContractResponse;
	}

	@Transactional
	public ChartOfAccountContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountContractRequest chartOfAccountContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("ChartOfAccountUpdateAll"), ChartOfAccountContractRequest.class);
		ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
		chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (chartOfAccountContractRequest.getChartOfAccounts() != null
				&& !chartOfAccountContractRequest.getChartOfAccounts().isEmpty()) {
			for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getChartOfAccounts()) {
				ChartOfAccount chartOfAccountEntity = new ChartOfAccount(chartOfAccountContract);
				chartOfAccountEntity.setVersion(findOne(chartOfAccountEntity.getId()).getVersion());
				chartOfAccountJpaRepository.save(chartOfAccountEntity);
				ChartOfAccountContract resp = modelMapper.map(chartOfAccountEntity, ChartOfAccountContract.class);
				chartOfAccountContractResponse.getChartOfAccounts().add(resp);
			}
		}
		chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
		return chartOfAccountContractResponse;
	}

	@Transactional
	public ChartOfAccountContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final ChartOfAccountContractRequest chartOfAccountContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("ChartOfAccountUpdate"), ChartOfAccountContractRequest.class);
		ChartOfAccountContractResponse chartOfAccountContractResponse = new ChartOfAccountContractResponse();
		chartOfAccountContractResponse.setChartOfAccounts(new ArrayList<ChartOfAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		ChartOfAccount chartOfAccountEntity = new ChartOfAccount(chartOfAccountContractRequest.getChartOfAccount());
		chartOfAccountEntity.setVersion(chartOfAccountJpaRepository.findOne(chartOfAccountEntity.getId()).getVersion());
		chartOfAccountJpaRepository.save(chartOfAccountEntity);
		ChartOfAccountContract resp = modelMapper.map(chartOfAccountEntity, ChartOfAccountContract.class);
		chartOfAccountContractResponse.setChartOfAccount(resp);
		chartOfAccountContractResponse.setResponseInfo(getResponseInfo(chartOfAccountContractRequest.getRequestInfo()));
		return chartOfAccountContractResponse;
	}

	@Transactional
	public ChartOfAccount create(final ChartOfAccount chartOfAccount) {
		setChartOfAccount(chartOfAccount);
		return chartOfAccountJpaRepository.save(chartOfAccount);
	}

	private void setChartOfAccount(final ChartOfAccount chartOfAccount) {
		if (chartOfAccount.getAccountCodePurpose() != null) {
			final AccountCodePurpose accountCodePurpose = null;// accountCodePurposeService.findOne(chartOfAccount.getAccountCodePurpose());
			if (accountCodePurpose == null)
				throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
						" Invalid accountCodePurpose");
			chartOfAccount.setAccountCodePurpose(accountCodePurpose.getId());
		}
		if (chartOfAccount.getParentId() != null) {
			final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId());
			if (parentId == null)
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			chartOfAccount.setParentId(parentId.getId());
		}
	}

	@Transactional
	public ChartOfAccount update(final ChartOfAccount chartOfAccount) {
		setChartOfAccount(chartOfAccount);
		return chartOfAccountJpaRepository.save(chartOfAccount);
	}

	public List<ChartOfAccount> findAll() {
		return chartOfAccountJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public ChartOfAccount findByName(final String name) {
		return chartOfAccountJpaRepository.findByName(name);
	}

	public ChartOfAccount findOne(final Long id) {
		return chartOfAccountJpaRepository.findOne(id);
	}

	public Page<ChartOfAccount> search(final ChartOfAccountContractRequest chartOfAccountContractRequest) {

		final ChartOfAccountSpecification specification = new ChartOfAccountSpecification(
				chartOfAccountContractRequest.getChartOfAccount());
		final Pageable page = new PageRequest(chartOfAccountContractRequest.getPage().getOffSet(),
				chartOfAccountContractRequest.getPage().getPageSize());
		Set<ChartOfAccount> chartOfAccountSet = new HashSet<ChartOfAccount>();
		Page<ChartOfAccount> chartOfAccounts = chartOfAccountJpaRepository.findAll(specification, page);
		if (chartOfAccountContractRequest.getChartOfAccount() != null
				&& chartOfAccountContractRequest.getChartOfAccount().getAccountCodePurpose() != null
				&& chartOfAccountContractRequest.getChartOfAccount().getAccountCodePurpose().getId() != null) {
			for (ChartOfAccount coa : chartOfAccounts) {
				chartOfAccountSet.add(coa);
				chartOfAccountContractRequest.setChartOfAccount(new ChartOfAccountContract());
				chartOfAccountContractRequest.getChartOfAccount().setGlcode(coa.getGlcode() + "%");
				chartOfAccountContractRequest.getChartOfAccount().setTenantId(coa.getTenantId());
				final ChartOfAccountSpecification specification1 = new ChartOfAccountSpecification(
						chartOfAccountContractRequest.getChartOfAccount());
				final Pageable page1 = new PageRequest(chartOfAccountContractRequest.getPage().getOffSet(),
						chartOfAccountContractRequest.getPage().getPageSize());
				for (ChartOfAccount temp : chartOfAccountJpaRepository.findAll(specification1, page1)) {
					chartOfAccountSet.add(temp);
				}
			}

		}
		if (chartOfAccountSet.isEmpty())
			return chartOfAccounts;
		else
			return new PageImpl<ChartOfAccount>(new ArrayList(chartOfAccountSet));
	}

	public BindingResult validate(final ChartOfAccountContractRequest chartOfAccountContractRequest,
			final String method, final BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccount(),
						"ChartOfAccount to edit must not be null");
				validator.validate(chartOfAccountContractRequest.getChartOfAccount(), errors);
				break;
			case "view":
				// validator.validate(chartOfAccountContractRequest.getChartOfAccount(),
				// errors);
				break;
			case "create":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
						"ChartOfAccounts to create must not be null");
				for (final ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
						"ChartOfAccounts to create must not be null");
				for (final ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(chartOfAccountContractRequest.getRequestInfo(), errors);
			}
		} catch (final IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public ChartOfAccountContractRequest fetchRelatedContracts(
			final ChartOfAccountContractRequest chartOfAccountContractRequest) {
		final ModelMapper model = new ModelMapper();
		for (final ChartOfAccountContract chartOfAccount : chartOfAccountContractRequest.getChartOfAccounts()) {
			if (chartOfAccount.getAccountCodePurpose() != null) {
				final AccountCodePurpose accountCodePurpose = null;// accountCodePurposeService.findOne(chartOfAccount.getAccountCodePurpose().getId());
				if (accountCodePurpose == null)
					throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
							" Invalid accountCodePurpose");
				model.map(accountCodePurpose, chartOfAccount.getAccountCodePurpose());
			}
			if (chartOfAccount.getParentId() != null) {
				final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId().getId());
				if (parentId == null)
					throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
				model.map(parentId, chartOfAccount.getParentId());
			}
		}
		final ChartOfAccountContract chartOfAccount = chartOfAccountContractRequest.getChartOfAccount();
		if (chartOfAccount.getAccountCodePurpose() != null) {
			final AccountCodePurpose accountCodePurpose = null;// accountCodePurposeService.findOne(chartOfAccount.getAccountCodePurpose().getId());
			if (accountCodePurpose == null)
				throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
						" Invalid accountCodePurpose");
			model.map(accountCodePurpose, chartOfAccount.getAccountCodePurpose());
		}
		if (chartOfAccount.getParentId() != null) {
			final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId().getId());
			if (parentId == null)
				throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
			model.map(parentId, chartOfAccount.getParentId());
		}
		return chartOfAccountContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}