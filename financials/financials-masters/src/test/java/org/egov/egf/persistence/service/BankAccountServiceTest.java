package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.repository.BankAccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class BankAccountServiceTest {
	@Mock
	private BankAccountRepository bankAccountRepository;
	@InjectMocks
	private BankAccountService bankAccountService;

	@Test
	public void test_should_find_all() {
		List<BankAccount> expectedResult = new ArrayList<BankAccount>();
		expectedResult.add(new BankAccount());
		when(bankAccountRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<BankAccount> bankAccount = bankAccountService.findAll();
		assertEquals(expectedResult.size(), bankAccount.size());
	}

	@Test
	public void test_should_find_one() {
		BankAccount expectedResult = new BankAccount();
		when(bankAccountRepository.findOne(1233L)).thenReturn(expectedResult);
		final BankAccount bankAccount = bankAccountService.findOne(1233L);
		assertEquals(expectedResult, bankAccount);
	}

	@Test
	public void test_for_create() {
		BankAccount expectedResult = new BankAccount();
		when(bankAccountRepository.save(new BankAccount())).thenReturn(expectedResult);
		final BankAccount bankAccount = bankAccountService.create(expectedResult);
		assertSame(expectedResult, bankAccount);

	}

	@Test
	public void test_for_update() {
		BankAccount expectedResult = new BankAccount();
		when(bankAccountRepository.save(new BankAccount())).thenReturn(expectedResult);
		final BankAccount bankAccount = bankAccountService.update(expectedResult);
		assertSame(expectedResult, bankAccount);

	}

}
