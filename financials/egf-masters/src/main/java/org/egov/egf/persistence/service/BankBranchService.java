package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BankBranchJpaRepository;
import org.egov.egf.persistence.repository.BankBranchQueueRepository;
import org.egov.egf.persistence.specification.BankBranchSpecification;
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
public class BankBranchService {

	private final BankBranchJpaRepository bankBranchJpaRepository;
	private final BankBranchQueueRepository bankBranchQueueRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public BankBranchService(final BankBranchJpaRepository bankBranchJpaRepository,
			BankBranchQueueRepository bankBranchQueueRepository) {
		this.bankBranchJpaRepository = bankBranchJpaRepository;
		this.bankBranchQueueRepository = bankBranchQueueRepository;
	}

	@Autowired
	private SmartValidator validator;
	@Autowired
	private BankService bankService;

	public void push(final BankBranchContractRequest bankBranchContractRequest) {
		bankBranchQueueRepository.push(bankBranchContractRequest);
	}

	@Transactional
	public BankBranchContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final BankBranchContractRequest bankBranchContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankBranchCreate"), BankBranchContractRequest.class);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (bankBranchContractRequest.getBankBranches() != null
				&& !bankBranchContractRequest.getBankBranches().isEmpty()) {
			for (BankBranchContract bankBranchContract : bankBranchContractRequest.getBankBranches()) {
				BankBranch bankBranchEntity = new BankBranch(bankBranchContract);
				bankBranchJpaRepository.save(bankBranchEntity);
				BankBranchContract resp = modelMapper.map(bankBranchEntity, BankBranchContract.class);
				bankBranchContractResponse.getBankBranches().add(resp);
			}
		} else if (bankBranchContractRequest.getBankBranch() != null) {
			BankBranch bankBranchEntity = new BankBranch(bankBranchContractRequest.getBankBranch());
			bankBranchJpaRepository.save(bankBranchEntity);
			BankBranchContract resp = modelMapper.map(bankBranchEntity, BankBranchContract.class);
			bankBranchContractResponse.setBankBranch(resp);
		}
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		return bankBranchContractResponse;
	}

	@Transactional
	public BankBranchContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final BankBranchContractRequest bankBranchContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankBranchUpdate"), BankBranchContractRequest.class);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		ModelMapper modelMapper = new ModelMapper();

		BankBranch bankBranchEntity = new BankBranch(bankBranchContractRequest.getBankBranch());
		bankBranchEntity.setVersion(bankBranchJpaRepository.findOne(bankBranchEntity.getId()).getVersion());
		bankBranchJpaRepository.save(bankBranchEntity);
		BankBranchContract resp = modelMapper.map(bankBranchEntity, BankBranchContract.class);
		bankBranchContractResponse.setBankBranch(resp);
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		return bankBranchContractResponse;
	}

	@Transactional
	public BankBranch create(final BankBranch bankBranch) {
		setBankBranch(bankBranch);
		return bankBranchJpaRepository.save(bankBranch);
	}

	@Transactional
	public BankBranch update(final BankBranch bankBranch) {
		setBankBranch(bankBranch);
		return bankBranchJpaRepository.save(bankBranch);
	}

	private void setBankBranch(final BankBranch bankBranch) {
		if (bankBranch.getBank() != null) {
			Bank bank = bankService.findOne(bankBranch.getBank());
			if (bank == null) {
				throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
			}
			bankBranch.setBank(bank.getId());
		}
	}

	public List<BankBranch> findAll() {
		return bankBranchJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public BankBranch findByName(String name) {
		return bankBranchJpaRepository.findByName(name);
	}

	public BankBranch findByCode(String code) {
		return bankBranchJpaRepository.findByCode(code);
	}

	public BankBranch findOne(Long id) {
		return bankBranchJpaRepository.findOne(id);
	}

	public Page<BankBranch> search(BankBranchContractRequest bankBranchContractRequest) {
		final BankBranchSpecification specification = new BankBranchSpecification(
				bankBranchContractRequest.getBankBranch());
		Pageable page = new PageRequest(bankBranchContractRequest.getPage().getOffSet(),
				bankBranchContractRequest.getPage().getPageSize());
		return bankBranchJpaRepository.findAll(specification, page);
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
				Assert.notNull(bankBranchContractRequest.getBankBranches(), "BankBranches to create must not be null");
				for (BankBranchContract b : bankBranchContractRequest.getBankBranches()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(bankBranchContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public BankBranchContractRequest fetchRelatedContracts(BankBranchContractRequest bankBranchContractRequest) {
		ModelMapper model = new ModelMapper();
		for (BankBranchContract bankBranch : bankBranchContractRequest.getBankBranches()) {
			if (bankBranch.getBank() != null) {
				Bank bank = bankService.findOne(bankBranch.getBank().getId());
				if (bank == null) {
					throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
				}
				model.map(bank, bankBranch.getBank());
			}
		}
		BankBranchContract bankBranch = bankBranchContractRequest.getBankBranch();
		if (bankBranch.getBank() != null) {
			Bank bank = bankService.findOne(bankBranch.getBank().getId());
			if (bank == null) {
				throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
			}
			model.map(bank, bankBranch.getBank());
		}
		return bankBranchContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}