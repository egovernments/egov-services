package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.repository.FunctionaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FunctionServiceTest {
	@Mock
	private FunctionaryRepository functionaryRepository;

	@InjectMocks
	private FunctionaryService functionaryService;

	@Test
	public void test_should_find_all() {
		List<Functionary> expectedResult = new ArrayList<Functionary>();
		expectedResult.add(new Functionary());
		when(functionaryRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Functionary> Functionary = functionaryService.findAll();
		assertEquals(expectedResult.size(), Functionary.size());
	}

	@Test
	public void test_find_name() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.findByName("abc")).thenReturn(expectedResult);
		final Functionary Functionary = functionaryService.findByName("abc");
		assertEquals(expectedResult, Functionary);
	}

	@Test
	public void test_should_find_one() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.findOne(1233L)).thenReturn(expectedResult);
		final Functionary Functionary = functionaryService.findOne(1233L);
		assertEquals(expectedResult, Functionary);
	}

	@Test
	public void test_should_findby_code() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.findByCode("abc")).thenReturn(expectedResult);
		final Functionary Functionary = functionaryService.findByCode("abc");
		assertEquals(expectedResult, Functionary);
	}

	@Test
	public void test_for_create() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.save(new Functionary())).thenReturn(expectedResult);
		final Functionary functionary = functionaryService.create(expectedResult);
		assertSame(expectedResult, functionary);

	}

	@Test
	public void test_for_update() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.save(new Functionary())).thenReturn(expectedResult);
		final Functionary functionary = functionaryService.update(expectedResult);
		assertSame(expectedResult, functionary);

	}
}
