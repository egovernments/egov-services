package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.repository.BankRepository;
import org.egov.egf.persistence.specification.BankSpecification;
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

	private final BankRepository bankRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SmartValidator validator;

	@Autowired
	public BankService(final BankRepository bankRepository) {
		this.bankRepository = bankRepository;
	}

	@Transactional
	public Bank create(final Bank bank) {
		return bankRepository.save(bank);
	}

	@Transactional
	public Bank update(final Bank bank) {
		return bankRepository.save(bank);
	}

	public List<Bank> findAll() {
		return bankRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public Bank findByName(String name) {
		return bankRepository.findByName(name);
	}

	public Bank findByCode(String code) {
		return bankRepository.findByCode(code);
	}

	public Bank findOne(Long id) {
		return bankRepository.findOne(id);
	}

	public Page<Bank> search(BankContractRequest bankContractRequest) {
		final BankSpecification specification = new BankSpecification(bankContractRequest.getBank());
		Pageable page = new PageRequest(bankContractRequest.getPage().getOffSet(),
				bankContractRequest.getPage().getPageSize());
		return bankRepository.findAll(specification, page);
	}

	public BindingResult validate(BankContractRequest bankContractRequest, String method,BindingResult errors) {
	
		try {
			switch(method)
			{
			case "update":
				Assert.notNull(bankContractRequest.getBank(), "Bank to edit must not be null");
				validator.validate(bankContractRequest.getBank(), errors);
				break;
			case "view":
				//validator.validate(bankContractRequest.getBank(), errors);
				break;
			case "create":
				Assert.notNull(bankContractRequest.getBanks(), "Banks to create must not be null");
				for(BankContract b:bankContractRequest.getBanks())
				 validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(bankContractRequest.getBanks(), "Banks to create must not be null");
				for(BankContract b:bankContractRequest.getBanks())
				 validator.validate(b, errors);
				break;
			default : validator.validate(bankContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			 errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

}