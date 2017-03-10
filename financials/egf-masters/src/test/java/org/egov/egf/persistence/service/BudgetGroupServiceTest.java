package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.repository.BudgetGroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class BudgetGroupServiceTest {
	@Mock
	private BudgetGroupRepository budgetGroupRepository;
	@InjectMocks
	private BudgetGroupService budgetGroupService;

	@Test
	public void test_should_find_all() {
		List<BudgetGroup> expectedResult = new ArrayList<BudgetGroup>();
		expectedResult.add(new BudgetGroup());
		when(budgetGroupRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<BudgetGroup> BudgetGroup = budgetGroupService.findAll();

		assertEquals(expectedResult.size(), BudgetGroup.size());
	}

	@Test
	public void test_find_name() {
		BudgetGroup expectedResult = new BudgetGroup();
		when(budgetGroupRepository.findByName("abc")).thenReturn(expectedResult);
		final BudgetGroup BudgetGroup = budgetGroupService.findByName("abc");
		assertEquals(expectedResult, BudgetGroup);
	}

	@Test
	public void test_should_find_one() {
		BudgetGroup expectedResult = new BudgetGroup();
		when(budgetGroupRepository.findOne(1233L)).thenReturn(expectedResult);
		final BudgetGroup BudgetGroup = budgetGroupService.findOne(1233L);
		assertEquals(expectedResult, BudgetGroup);
	}

	@Test
	public void test_for_create() {
		BudgetGroup expectedResult = new BudgetGroup();
		when(budgetGroupRepository.save(new BudgetGroup())).thenReturn(expectedResult);
		final BudgetGroup budgetGroup = budgetGroupService.create(expectedResult);
		assertSame(expectedResult, budgetGroup);

	}

	@Test
	public void test_for_update() {
		BudgetGroup expectedResult = new BudgetGroup();
		when(budgetGroupRepository.save(new BudgetGroup())).thenReturn(expectedResult);
		final BudgetGroup budgetGroup = budgetGroupService.update(expectedResult);
		assertSame(expectedResult, budgetGroup);

	}

}
