package org.egov.egf.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.repository.BankRepository;
import org.egov.egf.persistence.specification.BankSpecification;
import org.egov.egf.web.contract.BankContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BankService {

	private final BankRepository bankRepository;
	@PersistenceContext
	private EntityManager entityManager;

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

	public List<Bank> search(Bank bank) {
		return bankRepository.findAll();
	}

	public List<Bank> findAll(BankContract criteria) {
		final BankSpecification specification = new BankSpecification(criteria);
		return this.bankRepository.findAll(specification);
		/*
		 * .stream() .map(Bank::toDomain) .collect(Collectors.toList());
		 */
	}

}