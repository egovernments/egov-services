package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.repository.FundsourceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FundsourceServiceTest {
	@Mock
	private FundsourceRepository fundsourceRepository;

	@InjectMocks
	private FundsourceService fundsourceService;

	@Test
	public void test_should_find_all() {
		List<Fundsource> expectedResult = new ArrayList<Fundsource>();
		expectedResult.add(new Fundsource());
		when(fundsourceRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Fundsource> fundsource = fundsourceService.findAll();

		assertEquals(expectedResult.size(), fundsource.size());
	}

	@Test
	public void test_find_name() {
		Fundsource expectedResult = new Fundsource();
		when(fundsourceRepository.findByName("abc")).thenReturn(expectedResult);
		final Fundsource fundsource = fundsourceService.findByName("abc");
		assertEquals(expectedResult, fundsource);
	}

	@Test
	public void test_should_find_one() {
		Fundsource expectedResult = new Fundsource();
		when(fundsourceRepository.findOne(1233L)).thenReturn(expectedResult);
		final Fundsource fundsource = fundsourceService.findOne(1233L);
		assertEquals(expectedResult, fundsource);
	}

	@Test
	public void test_should_findby_code() {
		Fundsource expectedResult = new Fundsource();
		when(fundsourceRepository.findByCode("abc")).thenReturn(expectedResult);
		final Fundsource fundsource = fundsourceService.findByCode("abc");
		assertEquals(expectedResult, fundsource);
	}

	@Test
	public void test_for_create() {
		Fundsource expectedResult = new Fundsource();
		when(fundsourceRepository.save(new Fundsource())).thenReturn(expectedResult);
		final Fundsource fundsource = fundsourceService.create(expectedResult);
		assertSame(expectedResult, fundsource);

	}

	@Test
	public void test_for_update() {
		Fundsource expectedResult = new Fundsource();
		when(fundsourceRepository.save(new Fundsource())).thenReturn(expectedResult);
		final Fundsource fundsource = fundsourceService.update(expectedResult);
		assertSame(expectedResult, fundsource);

	}

}
