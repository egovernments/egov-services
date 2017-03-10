package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.repository.FiscalPeriodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FiscalPeriodServiceTest {
	@Mock
	private FiscalPeriodRepository fiscalPeriodRepository;

	@InjectMocks
	private FiscalPeriodService fiscalPeriodService;

	@Test
	public void test_should_find_all() {
		List<FiscalPeriod> expectedResult = new ArrayList<FiscalPeriod>();
		expectedResult.add(new FiscalPeriod());
		when(fiscalPeriodRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<FiscalPeriod> fiscalPeriod = fiscalPeriodService.findAll();
		assertEquals(expectedResult.size(), fiscalPeriod.size());
	}

	@Test
	public void test_find_name() {
		FiscalPeriod expectedResult = new FiscalPeriod();
		when(fiscalPeriodRepository.findByName("abc")).thenReturn(expectedResult);
		final FiscalPeriod fiscalPeriod = fiscalPeriodService.findByName("abc");
		assertEquals(expectedResult, fiscalPeriod);
	}

	@Test
	public void test_should_find_one() {
		FiscalPeriod expectedResult = new FiscalPeriod();
		when(fiscalPeriodRepository.findOne(1233L)).thenReturn(expectedResult);
		final FiscalPeriod fiscalPeriod = fiscalPeriodService.findOne(1233L);
		assertEquals(expectedResult, fiscalPeriod);
	}

	@Test
	public void test_for_create() {
		FiscalPeriod expectedResult = new FiscalPeriod();
		when(fiscalPeriodRepository.save(new FiscalPeriod())).thenReturn(expectedResult);
		final FiscalPeriod fiscalPeriod = fiscalPeriodService.create(expectedResult);
		assertSame(expectedResult, fiscalPeriod);

	}

	@Test
	public void test_for_update() {
		FiscalPeriod expectedResult = new FiscalPeriod();
		when(fiscalPeriodRepository.save(new FiscalPeriod())).thenReturn(expectedResult);
		final FiscalPeriod fiscalPeriod = fiscalPeriodService.update(expectedResult);
		assertSame(expectedResult, fiscalPeriod);

	}

}
