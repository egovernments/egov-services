package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.repository.FunctionaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class FunctionaryServiceTest {

	@Mock
	private FunctionaryRepository functionaryRepository;

	@Mock
	private EntityManager entityManager;

	@InjectMocks
	private FunctionaryService functionaryService;

	@Test
	public void test_should_find_all_accountcode() {
		List<Functionary> expectedResult = new ArrayList<Functionary>();
		expectedResult.add(new Functionary());
		when(functionaryRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Functionary> functionary = functionaryService.findAll();
		assertEquals(expectedResult.size(), functionary.size());
	}

	@Test
	public void test_find_name() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.findByName("abc")).thenReturn(expectedResult);
		final Functionary functionary = functionaryService.findByName("abc");
		assertEquals(expectedResult, functionary);
	}

	@Test
	public void test_should_find_one() {
		Functionary expectedResult = new Functionary();
		when(functionaryRepository.findOne(1233L)).thenReturn(expectedResult);
		final Functionary functionary = functionaryService.findOne(1233L);
		assertEquals(expectedResult, functionary);
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
