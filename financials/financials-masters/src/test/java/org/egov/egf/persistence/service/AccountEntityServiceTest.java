package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.AccountEntity;
import org.egov.egf.persistence.repository.AccountEntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class AccountEntityServiceTest {
	@Mock
	private AccountEntityRepository accountEntityRepository;
	@InjectMocks
	private AccountEntityService accountEntityService;

	@Test
	public void test_should_find_all() {
		List<AccountEntity> expectedResult = new ArrayList<AccountEntity>();
		expectedResult.add(new AccountEntity());
		when(accountEntityRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<AccountEntity> accountEntity = accountEntityService.findAll();
		assertEquals(expectedResult.size(), accountEntity.size());
	}

	@Test
	public void test_find_name() {
		AccountEntity expectedResult = new AccountEntity();
		when(accountEntityRepository.findByName("abc")).thenReturn(expectedResult);
		final AccountEntity accountEntity = accountEntityService.findByName("abc");
		assertEquals(expectedResult, accountEntity);
	}

	@Test
	public void test_should_find_one() {
		AccountEntity expectedResult = new AccountEntity();
		when(accountEntityRepository.findOne(1233L)).thenReturn(expectedResult);
		final AccountEntity accountEntity = accountEntityService.findOne(1233L);
		assertEquals(expectedResult, accountEntity);
	}

	@Test
	public void test_should_findby_code() {
		AccountEntity expectedResult = new AccountEntity();
		when(accountEntityRepository.findByCode("abc")).thenReturn(expectedResult);
		final AccountEntity accountEntity = accountEntityService.findByCode("abc");
		assertEquals(expectedResult, accountEntity);
	}

	@Test
	public void test_for_create() {
		AccountEntity expectedResult = new AccountEntity();
		when(accountEntityRepository.save(new AccountEntity())).thenReturn(expectedResult);
		final AccountEntity accountEntity = accountEntityService.create(expectedResult);
		assertSame(expectedResult, accountEntity);

	}

	@Test
	public void test_for_update() {
		AccountEntity expectedResult = new AccountEntity();
		when(accountEntityRepository.save(new AccountEntity())).thenReturn(expectedResult);
		final AccountEntity accountEntity = accountEntityService.update(expectedResult);
		assertSame(expectedResult, accountEntity);

	}

}
