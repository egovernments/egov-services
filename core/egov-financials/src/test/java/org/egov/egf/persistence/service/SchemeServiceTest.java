package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.repository.SchemeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class SchemeServiceTest {
	@Mock
	private SchemeRepository schemeRepository;

	@InjectMocks
	private SchemeService schemeService;

	@Test
	public void test_should_find_all() {
		List<Scheme> expectedResult = new ArrayList<Scheme>();
		expectedResult.add(new Scheme());
		when(schemeRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Scheme> scheme = schemeService.findAll();
		assertEquals(expectedResult.size(), scheme.size());
	}

	@Test
	public void test_find_name() {
		Scheme expectedResult = new Scheme();
		when(schemeRepository.findByName("abc")).thenReturn(expectedResult);
		final Scheme scheme = schemeService.findByName("abc");
		assertEquals(expectedResult, scheme);
	}

	@Test
	public void test_should_find_one() {
		Scheme expectedResult = new Scheme();
		when(schemeRepository.findOne(1233L)).thenReturn(expectedResult);
		final Scheme scheme = schemeService.findOne(1233L);
		assertEquals(expectedResult, scheme);
	}

	@Test
	public void test_should_findby_code() {
		Scheme expectedResult = new Scheme();
		when(schemeRepository.findByCode("abc")).thenReturn(expectedResult);
		final Scheme scheme = schemeService.findByCode("abc");
		assertEquals(expectedResult, scheme);
	}

	@Test
	public void test_for_create() {
		Scheme expectedResult = new Scheme();
		when(schemeRepository.save(new Scheme())).thenReturn(expectedResult);
		final Scheme scheme = schemeService.create(expectedResult);
		assertSame(expectedResult, scheme);

	}

	@Test
	public void test_for_update() {
		Scheme expectedResult = new Scheme();
		when(schemeRepository.save(new Scheme())).thenReturn(expectedResult);
		final Scheme scheme = schemeService.update(expectedResult);
		assertSame(expectedResult, scheme);

	}

}
