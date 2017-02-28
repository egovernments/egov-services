package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.repository.ChartOfAccountDetailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class ChartOfAccountDetailServiceTest {
	@Mock
	private ChartOfAccountDetailRepository chartOfAccountDetailRepository;
	@InjectMocks
	private ChartOfAccountDetailService chartOfAccountDetailService;

	@Test
	public void test_should_find_all() {
		List<ChartOfAccountDetail> expectedResult = new ArrayList<ChartOfAccountDetail>();
		expectedResult.add(new ChartOfAccountDetail());
		when(chartOfAccountDetailRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<ChartOfAccountDetail> chartOfAccountDetail = chartOfAccountDetailService.findAll();
		assertEquals(expectedResult.size(), chartOfAccountDetail.size());
	}

	@Test
	public void test_should_find_one() {
		ChartOfAccountDetail expectedResult = new ChartOfAccountDetail();
		when(chartOfAccountDetailRepository.findOne(1233L)).thenReturn(expectedResult);
		final ChartOfAccountDetail chartOfAccountDetail = chartOfAccountDetailService.findOne(1233L);
		assertEquals(expectedResult, chartOfAccountDetail);
	}

	@Test
	public void test_for_create() {
		ChartOfAccountDetail expectedResult = new ChartOfAccountDetail();
		when(chartOfAccountDetailRepository.save(new ChartOfAccountDetail())).thenReturn(expectedResult);
		final ChartOfAccountDetail chartOfAccountDetail = chartOfAccountDetailService.create(expectedResult);
		assertSame(expectedResult, chartOfAccountDetail);

	}

	@Test
	public void test_for_update() {
		ChartOfAccountDetail expectedResult = new ChartOfAccountDetail();
		when(chartOfAccountDetailRepository.save(new ChartOfAccountDetail())).thenReturn(expectedResult);
		final ChartOfAccountDetail chartOfAccountDetail = chartOfAccountDetailService.update(expectedResult);
		assertSame(expectedResult, chartOfAccountDetail);

	}

}
