package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.repository.SupplierRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)

public class SupplierServiceTest {
	@Mock
	private SupplierRepository supplierRepository;

	@InjectMocks
	private SupplierService supplierService;

	@Test
	public void test_should_find_all() {
		List<Supplier> expectedResult = new ArrayList<Supplier>();
		expectedResult.add(new Supplier());
		when(supplierRepository.findAll(any(Sort.class))).thenReturn(expectedResult);
		final List<Supplier> supplier = supplierService.findAll();
		assertEquals(expectedResult.size(), supplier.size());
	}

	@Test
	public void test_find_name() {
		Supplier expectedResult = new Supplier();
		when(supplierRepository.findByName("abc")).thenReturn(expectedResult);
		final Supplier supplier = supplierService.findByName("abc");
		assertEquals(expectedResult, supplier);
	}

	@Test
	public void test_should_find_one() {
		Supplier expectedResult = new Supplier();
		when(supplierRepository.findOne(1233L)).thenReturn(expectedResult);
		final Supplier supplier = supplierService.findOne(1233L);
		assertEquals(expectedResult, supplier);
	}

	@Test
	public void test_should_findby_code() {
		Supplier expectedResult = new Supplier();
		when(supplierRepository.findByCode("abc")).thenReturn(expectedResult);
		final Supplier supplier = supplierService.findByCode("abc");
		assertEquals(expectedResult, supplier);
	}

	@Test
	public void test_for_create() {
		Supplier expectedResult = new Supplier();
		when(supplierRepository.save(new Supplier())).thenReturn(expectedResult);
		final Supplier supplier = supplierService.create(expectedResult);
		assertSame(expectedResult, supplier);

	}

	@Test
	public void test_for_update() {
		Supplier expectedResult = new Supplier();
		when(supplierRepository.save(new Supplier())).thenReturn(expectedResult);
		final Supplier supplier = supplierService.update(expectedResult);
		assertSame(expectedResult, supplier);

	}

}
