package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.AccountDetailKey;
import org.egov.egf.persistence.repository.AccountDetailKeyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class AccountDetailKeyServiceTest {
	@Mock
	private AccountDetailKeyRepository accountDetailKeyRepository;
	@InjectMocks
	private AccountDetailKeyService accountDetailKeyService;

	@Test
	public void test_should_find_all() {
		List<AccountDetailKey> expectedResult = new ArrayList<AccountDetailKey>();
		expectedResult.add(new AccountDetailKey());
		when(accountDetailKeyRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<AccountDetailKey> accountDetailKey = accountDetailKeyService.findAll();
		assertEquals(expectedResult.size(), accountDetailKey.size());
	}

	@Test
	public void test_find_name() {
		AccountDetailKey expectedResult = new AccountDetailKey();
		when(accountDetailKeyRepository.findByName("abc")).thenReturn(expectedResult);
		final AccountDetailKey accountDetailKey = accountDetailKeyService.findByName("abc");
		assertEquals(expectedResult, accountDetailKey);
	}

	@Test
	public void test_should_find_one() {
		AccountDetailKey expectedResult = new AccountDetailKey();
		when(accountDetailKeyRepository.findOne(1233L)).thenReturn(expectedResult);
		final AccountDetailKey accountDetailKey = accountDetailKeyService.findOne(1233L);
		assertEquals(expectedResult, accountDetailKey);
	}

	@Test
	public void test_for_create() {
		AccountDetailKey expectedResult = new AccountDetailKey();
		when(accountDetailKeyRepository.save(new AccountDetailKey())).thenReturn(expectedResult);
		final AccountDetailKey accountDetailKey = accountDetailKeyService.create(expectedResult);
		assertSame(expectedResult, accountDetailKey);

	}

	@Test
	public void test_for_update() {
		AccountDetailKey expectedResult = new AccountDetailKey();
		when(accountDetailKeyRepository.save(new AccountDetailKey())).thenReturn(expectedResult);
		final AccountDetailKey accountDetailKey = accountDetailKeyService.update(expectedResult);
		assertSame(expectedResult, accountDetailKey);

	}

}
