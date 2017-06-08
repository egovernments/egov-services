package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.BankJpaRepository;
import org.egov.egf.persistence.repository.BankQueueRepository;
import org.egov.egf.persistence.specification.BankSpecification;
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
public class BankService {

	private final BankJpaRepository bankJpaRepository;
	private final BankQueueRepository bankQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public BankService(final BankJpaRepository bankJpaRepository, final BankQueueRepository bankQueueRepository) {
		this.bankJpaRepository = bankJpaRepository;
		this.bankQueueRepository = bankQueueRepository;
	}

	public void push(final BankContractRequest bankContractRequest) {
		bankQueueRepository.push(bankContractRequest);
	}

	@Transactional
	public BankContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final BankContractRequest bankContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankCreate"), BankContractRequest.class);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (bankContractRequest.getBanks() != null && !bankContractRequest.getBanks().isEmpty()) {
			for (BankContract bankContract : bankContractRequest.getBanks()) {
				Bank bankEntity = modelMapper.map(bankContract, Bank.class);
				bankJpaRepository.save(bankEntity);
				BankContract resp = modelMapper.map(bankEntity, BankContract.class);
				bankContractResponse.getBanks().add(resp);
			}
		} else if (bankContractRequest.getBank() != null) {
			Bank bankEntity = modelMapper.map(bankContractRequest.getBank(), Bank.class);
			bankJpaRepository.save(bankEntity);
			BankContract resp = modelMapper.map(bankEntity, BankContract.class);
			bankContractResponse.setBank(resp);
		}
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		return bankContractResponse;
	}

	@Transactional
	public BankContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final BankContractRequest bankContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankUpdateAll"), BankContractRequest.class);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (bankContractRequest.getBanks() != null && !bankContractRequest.getBanks().isEmpty()) {
			for (BankContract bankContract : bankContractRequest.getBanks()) {
				Bank bankEntity = modelMapper.map(bankContract, Bank.class);
				bankEntity.setVersion(findOne(bankEntity.getId()).getVersion());
				bankJpaRepository.save(bankEntity);
				BankContract resp = modelMapper.map(bankEntity, BankContract.class);
				bankContractResponse.getBanks().add(resp);
			}
		}
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		return bankContractResponse;
	}

	@Transactional
	public BankContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final BankContractRequest bankContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("BankUpdate"), BankContractRequest.class);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBank(new BankContract());
		ModelMapper modelMapper = new ModelMapper();
		Bank bankEntity = modelMapper.map(bankContractRequest.getBank(), Bank.class);
		bankEntity.setVersion(bankJpaRepository.findOne(bankEntity.getId()).getVersion());
		bankJpaRepository.save(bankEntity);
		BankContract resp = modelMapper.map(bankEntity, BankContract.class);
		bankContractResponse.setBank(resp);
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		return bankContractResponse;
	}

	@Transactional
	public Bank update(final Bank bank) {
		return bankJpaRepository.save(bank);
	}

	public List<Bank> findAll() {
		return bankJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Bank findByName(String name) {
		return bankJpaRepository.findByName(name);
	}

	public Bank findByCode(String code) {
		return bankJpaRepository.findByCode(code);
	}

	public Bank findOne(Long id) {
		return bankJpaRepository.findOne(id);
	}

	public Page<Bank> search(BankContractRequest bankContractRequest) {
		final BankSpecification specification = new BankSpecification(bankContractRequest.getBank());
		Pageable page = new PageRequest(bankContractRequest.getPage().getOffSet(),
				bankContractRequest.getPage().getPageSize());
		return bankJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(BankContractRequest bankContractRequest, String method, BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(bankContractRequest.getBank(), "Bank to edit must not be null");
				validator.validate(bankContractRequest.getBank(), errors);
				break;
			case "view":
				// validator.validate(bankContractRequest.getBank(), errors);
				break;
			case "create":
				Assert.notNull(bankContractRequest.getBanks(), "Banks to create must not be null");
				for (BankContract b : bankContractRequest.getBanks()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(bankContractRequest.getBanks(), "Banks to create must not be null");
				for (BankContract b : bankContractRequest.getBanks()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(bankContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public BankContractRequest fetchRelatedContracts(BankContractRequest bankContractRequest) {
		return bankContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}