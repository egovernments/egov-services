package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.repository.BankRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class BankServiceTest {
	@Mock
	private BankRepository bankRepository;
	@InjectMocks
	private BankService bankService;

	@Test
	public void test_should_find_all() {
		List<Bank> expectedResult = new ArrayList<Bank>();
		expectedResult.add(new Bank());
		when(bankRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Bank> bank = bankService.findAll();
		assertEquals(expectedResult.size(), bank.size());
	}

	@Test
	public void test_find_name() {
		Bank expectedResult = new Bank();
		when(bankRepository.findByName("abc")).thenReturn(expectedResult);
		final Bank bank = bankService.findByName("abc");
		assertEquals(expectedResult, bank);
	}

	@Test
	public void test_should_find_one() {
		Bank expectedResult = new Bank();
		when(bankRepository.findOne(1233L)).thenReturn(expectedResult);
		final Bank bank = bankService.findOne(1233L);
		assertEquals(expectedResult, bank);
	}

	@Test
	public void test_should_findby_code() {
		Bank expectedResult = new Bank();
		when(bankRepository.findByCode("abc")).thenReturn(expectedResult);
		final Bank bank = bankService.findByCode("abc");
		assertEquals(expectedResult, bank);
	}

	@Test
	public void test_for_create() {
		Bank expectedResult = new Bank();
		when(bankRepository.save(new Bank())).thenReturn(expectedResult);
		final Bank bank = bankService.create(expectedResult);
		assertSame(expectedResult, bank);

	}

	@Test
	public void test_for_update() {
		Bank expectedResult = new Bank();
		when(bankRepository.save(new Bank())).thenReturn(expectedResult);
		final Bank bank = bankService.update(expectedResult);
		assertSame(expectedResult, bank);

	}
}
