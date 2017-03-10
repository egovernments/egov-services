package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.repository.AccountDetailTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class AccountDetailTypeServiceTest {
	@Mock
	private AccountDetailTypeRepository accountDetailTypeRepository;
	@InjectMocks
	private AccountDetailTypeService accountDetailTypeService;

	@Test
	public void test_should_find_all() {
		List<AccountDetailType> expectedResult = new ArrayList<AccountDetailType>();
		expectedResult.add(new AccountDetailType());
		when(accountDetailTypeRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<AccountDetailType> accountDetailType = accountDetailTypeService.findAll();
		assertEquals(expectedResult.size(), accountDetailType.size());

	}

	@Test
	public void test_find_name() {
		AccountDetailType expectedResult = new AccountDetailType();
		when(accountDetailTypeRepository.findByName("abc")).thenReturn(expectedResult);
		final AccountDetailType accountDetailType = accountDetailTypeService.findByName("abc");
		assertEquals(expectedResult, accountDetailType);
	}

	@Test
	public void test_should_find_one() {
		AccountDetailType expectedResult = new AccountDetailType();
		when(accountDetailTypeRepository.findOne(1233L)).thenReturn(expectedResult);
		final AccountDetailType accountDetailTyp = accountDetailTypeService.findOne(1233L);
		assertEquals(expectedResult, accountDetailTyp);
	}

	@Test
	public void test_for_create() {
		AccountDetailType expectedResult = new AccountDetailType();
		when(accountDetailTypeRepository.save(new AccountDetailType())).thenReturn(expectedResult);
		final AccountDetailType accountDetailTyp = accountDetailTypeService.create(expectedResult);
		assertSame(expectedResult, accountDetailTyp);

	}

	@Test
	public void test_for_update() {
		AccountDetailType expectedResult = new AccountDetailType();
		when(accountDetailTypeRepository.save(new AccountDetailType())).thenReturn(expectedResult);
		final AccountDetailType accountDetailTyp = accountDetailTypeService.update(expectedResult);
		assertSame(expectedResult, accountDetailTyp);

	}

}
