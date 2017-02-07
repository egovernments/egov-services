package org.egov.egf.service;


import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.entity.BankEntity;
import org.egov.egf.repository.BankRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankService  {

	private final BankRepository bankRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public BankService(final BankRepository bankRepository) {
		this.bankRepository = bankRepository;
	}

	@Transactional
	public BankEntity create(final BankEntity bank) {
		return bankRepository.save(bank);
	} 
	@Transactional
	public BankEntity update(final BankEntity bank) {
		return bankRepository.save(bank);
	} 
	public List<BankEntity> findAll() {
		return bankRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}
	public BankEntity findByName(String name){
		return bankRepository.findByName(name);
	}
	public BankEntity findByCode(String code){
		return bankRepository.findByCode(code);
	}
	public BankEntity findOne(Long id){
		return bankRepository.findOne(id);
	}
	public List<BankEntity> search(BankEntity bank){
		return bankRepository.findAll();
	}
}