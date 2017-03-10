package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.repository.BankBranchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class BankBranchServiceTest {
	@Mock
	private BankBranchRepository bankBranchRepository;
	@InjectMocks
	private BankBranchService bankBranchService;

	@Test
	public void test_should_find_all() {
		List<BankBranch> expectedResult = new ArrayList<BankBranch>();
		expectedResult.add(new BankBranch());
		when(bankBranchRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<BankBranch> bankBranch = bankBranchService.findAll();
		assertEquals(expectedResult.size(), bankBranch.size());
	}

	@Test
	public void test_should_find_one() {
		BankBranch expectedResult = new BankBranch();
		when(bankBranchRepository.findOne(1233L)).thenReturn(expectedResult);
		final BankBranch bankBranch = bankBranchService.findOne(1233L);

		assertEquals(expectedResult, bankBranch);
	}

	@Test
	public void test_should_findby_code() {
		BankBranch expectedResult = new BankBranch();
		when(bankBranchRepository.findByCode("abc")).thenReturn(expectedResult);
		final BankBranch bankBranch = bankBranchService.findByCode("abc");
		assertEquals(expectedResult, bankBranch);
	}

	@Test
	public void test_should_findby_name() {
		BankBranch expectedResult = new BankBranch();
		when(bankBranchRepository.findByName("abc")).thenReturn(expectedResult);
		final BankBranch bankBranch = bankBranchService.findByName("abc");
		assertEquals(expectedResult, bankBranch);
	}

	@Test
	public void test_for_create() {
		BankBranch expectedResult = new BankBranch();
		when(bankBranchRepository.save(new BankBranch())).thenReturn(expectedResult);
		final BankBranch bankBranch = bankBranchService.create(expectedResult);
		assertSame(expectedResult, bankBranch);

	}

	@Test
	public void test_for_update() {
		BankBranch expectedResult = new BankBranch();
		when(bankBranchRepository.save(new BankBranch())).thenReturn(expectedResult);
		final BankBranch bankBranch = bankBranchService.update(expectedResult);
		assertSame(expectedResult, bankBranch);

	}

}
