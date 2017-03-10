package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.repository.AccountCodePurposeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class AccountCodePurposeServiceTest {
	@InjectMocks
	private AccountCodePurposeService accountCodePurposeService;
	@Mock
	private AccountCodePurposeRepository accountCodePurposeRepository;

	@Test
	public void test_should_find_all_accountcode() {
		List<AccountCodePurpose> expectedResult = new ArrayList<AccountCodePurpose>();
		expectedResult.add(new AccountCodePurpose());
		when(accountCodePurposeRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<AccountCodePurpose> accountCodePurpose = accountCodePurposeService.findAll();
		assertEquals(expectedResult.size(), accountCodePurpose.size());
	}

	@Test
	public void test_find_name() {
		AccountCodePurpose expectedResult = new AccountCodePurpose();
		when(accountCodePurposeRepository.findByName("abc")).thenReturn(expectedResult);
		final AccountCodePurpose accountCodePurpose = accountCodePurposeService.findByName("abc");
		assertEquals(expectedResult, accountCodePurpose);
	}

	@Test
	public void test_should_find_one() {
		AccountCodePurpose expectedResult = new AccountCodePurpose();
		when(accountCodePurposeRepository.findOne(1233L)).thenReturn(expectedResult);
		final AccountCodePurpose accountCodePurpose = accountCodePurposeService.findOne(1233L);
		assertEquals(expectedResult, accountCodePurpose);
	}

	@Test
	public void test_for_create() {
		AccountCodePurpose expectedResult = new AccountCodePurpose();
		when(accountCodePurposeRepository.save(new AccountCodePurpose())).thenReturn(expectedResult);
		final AccountCodePurpose accountCodePurpose = accountCodePurposeService.create(expectedResult);
		assertSame(expectedResult, accountCodePurpose);

	}

	@Test
	public void test_for_update() {
		AccountCodePurpose expectedResult = new AccountCodePurpose();
		when(accountCodePurposeRepository.save(new AccountCodePurpose())).thenReturn(expectedResult);
		final AccountCodePurpose accountCodePurpose = accountCodePurposeService.update(expectedResult);
		assertSame(expectedResult, accountCodePurpose);

	}

}
