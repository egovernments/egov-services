package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BankBranchQueueRepository;
import org.egov.egf.persistence.repository.BankBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BankBranchService {

	private final BankBranchRepository bankBranchRepository;
	private final BankBranchQueueRepository bankBranchQueueRepository;

	private SmartValidator validator;

	private BankService bankService;

	@Autowired
	public BankBranchService(final BankBranchRepository bankBranchRepository,
			BankBranchQueueRepository bankBranchQueueRepository,BankService bankService,SmartValidator validator) {
		this.bankBranchRepository = bankBranchRepository;
		this.bankBranchQueueRepository = bankBranchQueueRepository;
		this.bankService = bankService;
		this.validator = validator;
	}

	public void push(final BankBranchContractRequest bankBranchContractRequest) {
		bankBranchQueueRepository.push(bankBranchContractRequest);
	}

	@Transactional
	public BankBranchContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		BankBranchContractRequest bankBranchContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankBranchCreate"), BankBranchContractRequest.class);
		bankBranchContractRequest = bankBranchRepository.create(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		bankBranchContractResponse.getBankBranches().addAll(bankBranchContractRequest.getBankBranches());
		bankBranchContractResponse.setBankBranch(bankBranchContractRequest.getBankBranch());
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		return bankBranchContractResponse;

	}

	@Transactional
	public BankBranchContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		BankBranchContractRequest bankBranchContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankBranchUpdate"), BankBranchContractRequest.class);
		bankBranchContractRequest = bankBranchRepository.update(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		bankBranchContractResponse.getBankBranches().addAll(bankBranchContractRequest.getBankBranches());
		bankBranchContractResponse.setBankBranch(bankBranchContractRequest.getBankBranch());
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		return bankBranchContractResponse;
	}

	public List<BankBranchContract> getBankBranches(BankBranchGetRequest bankBranchGetRequest) {
		return bankBranchRepository.findForCriteria(bankBranchGetRequest);
	}

	public BindingResult validate(BankBranchContractRequest bankBranchContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(bankBranchContractRequest.getBankBranch(), "BankBranch to edit must not be null");
				validator.validate(bankBranchContractRequest.getBankBranch(), errors);
				break;
			case "view":
				// validator.validate(bankBranchContractRequest.getBankBranch(),
				// errors);
				break;
			case "create":
				Assert.notNull(bankBranchContractRequest.getBankBranches(), "BankBranches to create must not be null");
				for (BankBranchContract b : bankBranchContractRequest.getBankBranches()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(bankBranchContractRequest.getBankBranches(), "BankBranches to update must not be null");
				for (BankBranchContract b : bankBranchContractRequest.getBankBranches()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(bankBranchContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data ", e.getMessage()));
		}
		return errors;

	}

	public BankBranchContractRequest fetchRelatedContracts(BankBranchContractRequest bankBranchContractRequest) {
		for (BankBranchContract bankBranch : bankBranchContractRequest.getBankBranches()) {
			if (bankBranch.getBank() != null) {
				Bank bank = bankService.findOne(bankBranch.getBank().getId());
				if (bank == null) {
					throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
				}
				bankBranch.setBank(new BankContract(bank));
			}
		}
		BankBranchContract bankBranch = bankBranchContractRequest.getBankBranch();
		if (bankBranch.getBank() != null) {
			Bank bank = bankService.findOne(bankBranch.getBank().getId());
			if (bank == null) {
				throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
			}
			bankBranch.setBank(new BankContract(bank));
		}
		return bankBranchContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}