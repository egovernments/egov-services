package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.repository.ChartOfAccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class ChartOfAccountServiceTest {
	@Mock
	private ChartOfAccountRepository chartOfAccountRepository;

	@InjectMocks
	private ChartOfAccountService chartOfAccountService;

	@Test
	public void test_should_find_all() {
		List<ChartOfAccount> expectedResult = new ArrayList<ChartOfAccount>();
		expectedResult.add(new ChartOfAccount());
		when(chartOfAccountRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<ChartOfAccount> chartOfAccount = chartOfAccountService.findAll();
		assertEquals(expectedResult.size(), chartOfAccount.size());
	}

	@Test
	public void test_find_name() {
		ChartOfAccount expectedResult = new ChartOfAccount();
		when(chartOfAccountRepository.findByName("abc")).thenReturn(expectedResult);
		final ChartOfAccount chartOfAccount = chartOfAccountService.findByName("abc");
		assertEquals(expectedResult, chartOfAccount);
	}

	@Test
	public void test_should_find_one() {
		ChartOfAccount expectedResult = new ChartOfAccount();
		when(chartOfAccountRepository.findOne(1233L)).thenReturn(expectedResult);
		final ChartOfAccount chartOfAccount = chartOfAccountService.findOne(1233L);
		assertEquals(expectedResult, chartOfAccount);
	}

	@Test
	public void test_for_create() {
		ChartOfAccount expectedResult = new ChartOfAccount();
		when(chartOfAccountRepository.save(new ChartOfAccount())).thenReturn(expectedResult);
		final ChartOfAccount chartOfAccount = chartOfAccountService.create(expectedResult);
		assertSame(expectedResult, chartOfAccount);

	}

	@Test
	public void test_for_update() {
		ChartOfAccount expectedResult = new ChartOfAccount();
		when(chartOfAccountRepository.save(new ChartOfAccount())).thenReturn(expectedResult);
		final ChartOfAccount chartOfAccount = chartOfAccountService.update(expectedResult);
		assertSame(expectedResult, chartOfAccount);

	}
}
