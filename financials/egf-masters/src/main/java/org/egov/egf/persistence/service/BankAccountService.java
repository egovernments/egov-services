package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
import org.egov.egf.persistence.queue.contract.BankAccountContractRequest;
import org.egov.egf.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BankAccountJpaRepository;
import org.egov.egf.persistence.repository.BankAccountQueueRepository;
import org.egov.egf.persistence.specification.BankAccountSpecification;
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
public class BankAccountService {

	private final BankAccountJpaRepository bankAccountJpaRepository;
	private final BankAccountQueueRepository bankAccountQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private ChartOfAccountService chartOfAccountService;

	@Autowired
	private BankBranchService bankBranchService;

	@Autowired
	private FundService fundService;

	@Autowired
	public BankAccountService(final BankAccountJpaRepository bankAccountJpaRepository,
			final BankAccountQueueRepository bankAccountQueueRepository) {
		this.bankAccountJpaRepository = bankAccountJpaRepository;
		this.bankAccountQueueRepository = bankAccountQueueRepository;
	}

	@Transactional
	public BankAccount create(final BankAccount bankAccount) {
		setBankAccount(bankAccount);
		return bankAccountJpaRepository.save(bankAccount);
	}

	@Transactional
	public BankAccount update(final BankAccount bankAccount) {
		setBankAccount(bankAccount);
		return bankAccountJpaRepository.save(bankAccount);
	}

	@Transactional
	public BankAccountContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final BankAccountContractRequest bankAccountContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankAccountCreate"), BankAccountContractRequest.class);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (bankAccountContractRequest.getBankAccounts() != null
				&& !bankAccountContractRequest.getBankAccounts().isEmpty()) {
			for (BankAccountContract bankAccountContract : bankAccountContractRequest.getBankAccounts()) {
				BankAccount bankAccountEntity = new BankAccount(bankAccountContract);
				bankAccountJpaRepository.save(bankAccountEntity);
				BankAccountContract resp = modelMapper.map(bankAccountEntity, BankAccountContract.class);
				bankAccountContractResponse.getBankAccounts().add(resp);
			}
		} else if (bankAccountContractRequest.getBankAccount() != null) {
			BankAccount bankAccountEntity = new BankAccount(bankAccountContractRequest.getBankAccount());
			bankAccountJpaRepository.save(bankAccountEntity);
			BankAccountContract resp = modelMapper.map(bankAccountEntity, BankAccountContract.class);
			bankAccountContractResponse.setBankAccount(resp);
		}
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		return bankAccountContractResponse;
	}

	@Transactional
	public BankAccountContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final BankAccountContractRequest bankAccountContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("BankAccountUpdateAll"), BankAccountContractRequest.class);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (bankAccountContractRequest.getBankAccounts() != null
				&& !bankAccountContractRequest.getBankAccounts().isEmpty()) {
			for (BankAccountContract bankAccountContract : bankAccountContractRequest.getBankAccounts()) {
				BankAccount bankAccountEntity = new BankAccount(bankAccountContract);
				bankAccountEntity.setVersion(findOne(bankAccountEntity.getId()).getVersion());
				bankAccountJpaRepository.save(bankAccountEntity);
				BankAccountContract resp = modelMapper.map(bankAccountEntity, BankAccountContract.class);
				bankAccountContractResponse.getBankAccounts().add(resp);
			}
		}
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		return bankAccountContractResponse;
	}

	@Transactional
	public BankAccountContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final BankAccountContractRequest bankAccountContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankAccountUpdate"), BankAccountContractRequest.class);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		ModelMapper modelMapper = new ModelMapper();
		BankAccount bankAccountEntity = new BankAccount(bankAccountContractRequest.getBankAccount());
		bankAccountEntity.setVersion(bankAccountJpaRepository.findOne(bankAccountEntity.getId()).getVersion());
		bankAccountJpaRepository.save(bankAccountEntity);
		BankAccountContract resp = modelMapper.map(bankAccountEntity, BankAccountContract.class);
		bankAccountContractResponse.setBankAccount(resp);
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		return bankAccountContractResponse;
	}

	public void push(final BankAccountContractRequest bankAccountContractRequest) {
		bankAccountQueueRepository.push(bankAccountContractRequest);
	}

	private void setBankAccount(final BankAccount bankAccount) {
		if (bankAccount.getBankBranch() != null) {
			BankBranchGetRequest bankBranchGetRequest = new BankBranchGetRequest();
			bankBranchGetRequest.setId(new ArrayList<Long>());
			bankBranchGetRequest.getId().add(bankAccount.getBankBranch());
			List<BankBranchContract> bankBranches = bankBranchService.getBankBranches(bankBranchGetRequest);
			BankBranch bankBranch;
			if (bankBranches != null && !bankBranches.isEmpty())
				bankBranch = new BankBranch(bankBranches.get(0));
			else
				bankBranch = null;
			if (bankBranch == null)
				throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
			bankAccount.setBankBranch(bankBranch.getId());
		}
		if (bankAccount.getChartOfAccount() != null) {
			final ChartOfAccount chartOfAccount = chartOfAccountService.findOne(bankAccount.getChartOfAccount());
			if (chartOfAccount == null)
				throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
			bankAccount.setChartOfAccount(chartOfAccount.getId());
		}
		if (bankAccount.getFund() != null) {
			final Fund fund = fundService.findOne(bankAccount.getFund());
			if (fund == null)
				throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
			bankAccount.setFund(fund.getId());
		}
	}

	public List<BankAccount> findAll() {
		return bankAccountJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public BankAccount findOne(final Long id) {
		return bankAccountJpaRepository.findOne(id);
	}

	public Page<BankAccount> search(final BankAccountContractRequest bankAccountContractRequest) {
		final BankAccountSpecification specification = new BankAccountSpecification(
				bankAccountContractRequest.getBankAccount());
		final Pageable page = new PageRequest(bankAccountContractRequest.getPage().getOffSet(),
				bankAccountContractRequest.getPage().getPageSize());
		return bankAccountJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(final BankAccountContractRequest bankAccountContractRequest, final String method,
			final BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(bankAccountContractRequest.getBankAccount(), "BankAccount to edit must not be null");
				validator.validate(bankAccountContractRequest.getBankAccount(), errors);
				break;
			case "view":
				// validator.validate(bankAccountContractRequest.getBankAccount(),
				// errors);
				break;
			case "create":
				Assert.notNull(bankAccountContractRequest.getBankAccounts(), "BankAccounts to create must not be null");
				for (final BankAccountContract b : bankAccountContractRequest.getBankAccounts())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(bankAccountContractRequest.getBankAccounts(), "BankAccounts to create must not be null");
				for (final BankAccountContract b : bankAccountContractRequest.getBankAccounts())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(bankAccountContractRequest.getRequestInfo(), errors);
			}
		} catch (final IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public BankAccountContractRequest fetchRelatedContracts(
			final BankAccountContractRequest bankAccountContractRequest) {
		final ModelMapper model = new ModelMapper();
		for (final BankAccountContract bankAccount : bankAccountContractRequest.getBankAccounts()) {
			if (bankAccount.getBankBranch() != null) {
				BankBranchGetRequest bankBranchGetRequest = new BankBranchGetRequest();
				bankBranchGetRequest.setId(new ArrayList<Long>());
				bankBranchGetRequest.getId().add(bankAccount.getBankBranch().getId());
				List<BankBranchContract> bankBranches = bankBranchService.getBankBranches(bankBranchGetRequest);
				BankBranch bankBranch;
				if (bankBranches != null && !bankBranches.isEmpty())
					bankBranch = new BankBranch(bankBranches.get(0));
				else
					bankBranch = null;
				if (bankBranch == null)
					throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
				model.map(bankBranch, bankAccount.getBankBranch());
			}
			if (bankAccount.getChartOfAccount() != null) {
				final ChartOfAccount chartOfAccount = chartOfAccountService
						.findOne(bankAccount.getChartOfAccount().getId());
				if (chartOfAccount == null)
					throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
							" Invalid chartOfAccount");
				model.map(chartOfAccount, bankAccount.getChartOfAccount());
			}
			if (bankAccount.getFund() != null) {
				final Fund fund = fundService.findOne(bankAccount.getFund().getId());
				if (fund == null)
					throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
				model.map(fund, bankAccount.getFund());
			}

		}
		final BankAccountContract bankAccount = bankAccountContractRequest.getBankAccount();
		if (bankAccount.getBankBranch() != null) {
			BankBranchGetRequest bankBranchGetRequest = new BankBranchGetRequest();
			bankBranchGetRequest.setId(new ArrayList<Long>());
			bankBranchGetRequest.getId().add(bankAccount.getBankBranch().getId());
			List<BankBranchContract> bankBranches = bankBranchService.getBankBranches(bankBranchGetRequest);
			BankBranch bankBranch;
			if (bankBranches != null && !bankBranches.isEmpty())
				bankBranch = new BankBranch(bankBranches.get(0));
			else
				bankBranch = null;
			if (bankBranch == null)
				throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
			model.map(bankBranch, bankAccount.getBankBranch());
		}
		if (bankAccount.getChartOfAccount() != null) {
			final ChartOfAccount chartOfAccount = chartOfAccountService
					.findOne(bankAccount.getChartOfAccount().getId());
			if (chartOfAccount == null)
				throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
			model.map(chartOfAccount, bankAccount.getChartOfAccount());
		}
		if (bankAccount.getFund() != null) {
			final Fund fund = fundService.findOne(bankAccount.getFund().getId());
			if (fund == null)
				throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
			model.map(fund, bankAccount.getFund());
		}

		return bankAccountContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}