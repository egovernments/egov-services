package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.repository.FinancialYearRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FinancialYearServiceTest {
	@Mock
	private FinancialYearRepository financialYearRepository;

	@InjectMocks
	private FinancialYearService financialYearService;

	@Test
	public void test_should_find_all() {
		List<FinancialYear> expectedResult = new ArrayList<FinancialYear>();
		expectedResult.add(new FinancialYear());
		when(financialYearRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<FinancialYear> financialYear = financialYearService.findAll();
		assertEquals(expectedResult.size(), financialYear.size());
	}

	@Test
	public void test_should_find_one() {
		FinancialYear expectedResult = new FinancialYear();
		when(financialYearRepository.findOne(1233L)).thenReturn(expectedResult);
		final FinancialYear financialYear = financialYearService.findOne(1233L);
		assertEquals(expectedResult, financialYear);
	}

	@Test
	public void test_for_create() {
		FinancialYear expectedResult = new FinancialYear();
		when(financialYearRepository.save(new FinancialYear())).thenReturn(expectedResult);
		final FinancialYear financialYear = financialYearService.create(expectedResult);
		assertSame(expectedResult, financialYear);

	}

	@Test
	public void test_for_update() {
		FinancialYear expectedResult = new FinancialYear();
		when(financialYearRepository.save(new FinancialYear())).thenReturn(expectedResult);
		final FinancialYear financialYear = financialYearService.update(expectedResult);
		assertSame(expectedResult, financialYear);

	}
}
