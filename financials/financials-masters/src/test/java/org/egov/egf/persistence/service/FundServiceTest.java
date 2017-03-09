package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.repository.FundRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FundServiceTest {
	@Mock
	private FundRepository fundRepository;
	@InjectMocks
	private FundService fundService;

	@Test
	public void test_should_find_all() {
		List<Fund> expectedResult = new ArrayList<Fund>();
		expectedResult.add(new Fund());
		when(fundRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Fund> fund = fundService.findAll();
		assertEquals(expectedResult.size(), fund.size());
	}

	@Test
	public void test_find_name() {
		Fund expectedResult = new Fund();
		when(fundRepository.findByName("abc")).thenReturn(expectedResult);
		final Fund fund = fundService.findByName("abc");
		assertEquals(expectedResult, fund);
	}

	@Test
	public void test_should_find_one() {
		Fund expectedResult = new Fund();
		when(fundRepository.findOne(1233L)).thenReturn(expectedResult);
		final Fund fund = fundService.findOne(1233L);
		assertEquals(expectedResult, fund);
	}

	@Test
	public void test_should_findby_code() {
		Fund expectedResult = new Fund();
		when(fundRepository.findByCode("abc")).thenReturn(expectedResult);
		final Fund fund = fundService.findByCode("abc");
		assertEquals(expectedResult, fund);
	}

	@Test
	public void test_for_create() {
		Fund expectedResult = new Fund();
		when(fundRepository.save(new Fund())).thenReturn(expectedResult);
		final Fund fund = fundService.create(expectedResult);
		assertSame(expectedResult, fund);

	}

	@Test
	public void test_for_update() {
		Fund expectedResult = new Fund();
		when(fundRepository.save(new Fund())).thenReturn(expectedResult);
		final Fund fund = fundService.update(expectedResult);
		assertSame(expectedResult, fund);

	}
}
