package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.repository.SubSchemeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class SubSchemeServiceTest {
	@Mock
	private SubSchemeRepository subSchemeRepository;

	@InjectMocks
	private SubSchemeService subSchemeService;

	@Test
	public void test_should_find_all() {
		List<SubScheme> expectedResult = new ArrayList<SubScheme>();
		expectedResult.add(new SubScheme());
		when(subSchemeRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<SubScheme> subScheme = subSchemeService.findAll();
		assertEquals(expectedResult.size(), subScheme.size());
	}

	@Test
	public void test_find_name() {
		SubScheme expectedResult = new SubScheme();
		when(subSchemeRepository.findByName("abc")).thenReturn(expectedResult);
		final SubScheme subScheme = subSchemeService.findByName("abc");
		assertEquals(expectedResult, subScheme);
	}

	@Test
	public void test_should_find_one() {
		SubScheme expectedResult = new SubScheme();
		when(subSchemeRepository.findOne(1233L)).thenReturn(expectedResult);
		final SubScheme subScheme = subSchemeService.findOne(1233L);
		assertEquals(expectedResult, subScheme);
	}

	@Test
	public void test_should_findby_code() {
		SubScheme expectedResult = new SubScheme();
		when(subSchemeRepository.findByCode("abc")).thenReturn(expectedResult);
		final SubScheme subScheme = subSchemeService.findByCode("abc");
		assertEquals(expectedResult, subScheme);
	}

	@Test
	public void test_for_create() {
		SubScheme expectedResult = new SubScheme();
		when(subSchemeRepository.save(new SubScheme())).thenReturn(expectedResult);
		final SubScheme subScheme = subSchemeService.create(expectedResult);
		assertSame(expectedResult, subScheme);

	}

	@Test
	public void test_for_update() {
		SubScheme expectedResult = new SubScheme();
		when(subSchemeRepository.save(new SubScheme())).thenReturn(expectedResult);
		final SubScheme subScheme = subSchemeService.update(expectedResult);
		assertSame(expectedResult, subScheme);

	}

}
