package org.egov.egf.master.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.repository.FunctionRepository;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.requests.FunctionRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class FunctionServiceTest {

	@InjectMocks
	private FunctionService functionService;

	@Mock
	private SmartValidator validator;

	@Mock
	private FunctionRepository functionRepository;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private List<Function> functions = new ArrayList<>();

	@Test
	public final void test_valid_add() {

		Function expected = getFunction();
		expected.setParentId(null);
		functions.add(expected);
		List<Function> actual = functionService.add(functions, errors);
		assertEquals(expected, actual.get(0));
	}

	@Test
	public final void test_valid_update() {

		Function expected = getFunction();
		expected.setParentId(null);
		functions.add(expected);
		List<Function> actual = functionService.update(functions, errors);
		assertEquals(expected, actual.get(0));
	}

	@Test
	public final void test_add_to_queue() {

		FunctionRequest request = new FunctionRequest();
		List<FunctionContract> functions = new ArrayList<>();

		FunctionContract function1 = getFunctionContract();
		function1.setParentId(null);
		functions.add(function1);
		request.setFunctions(functions);
		functionService.addToQue(request);
		verify(functionRepository).add(request);
	}

	@Test
	public final void test_validate() {

		List<Function> functions = new ArrayList<>();

		Function function1 = getFunction();
		functions.add(function1);
		//functionService.validate(functions, "create", errors);
	//	functionService.validate(functions, "update", errors);
	}

	@Test
	public final void test_invalid_Add() {

		Function function1 = Function.builder().id("1").code("001").active(true).parentId(null).level(1).isParent(false)
				.build();
		functions.add(function1);

		functionService.add(functions, errors);
	}

	@Test
	public final void test_save() {
		Function expextedResult = getFunction();
		when(functionRepository.save(any(Function.class))).thenReturn(expextedResult);
		Function actualResult = functionService.save(getFunction());
		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {
		Function expextedResult = getFunction();
		when(functionRepository.update(any(Function.class))).thenReturn(expextedResult);
		Function actualResult = functionService.update(getFunction());
		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_search() {
		List<Function> search = new ArrayList<>();
		search.add(getFunctionSearch());
		Pagination<Function> expextedResult = new Pagination<>();
		expextedResult.setPagedData(search);
		when(functionRepository.search(any(FunctionSearch.class))).thenReturn(expextedResult);
		Pagination<Function> actualResult = functionService.search(getFunctionSearch());
		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_fetch_related_data() {
		List<Function> expextedResult = new ArrayList<>();
		expextedResult.add(getParentFunction());
		List<Function> functions = new ArrayList<>();
		functions.add(getFunction());
		when(functionRepository.findById(any(Function.class))).thenReturn(getParentFunction());
		List<Function> actualResult = functionService.fetchRelated(functions);
		assertEquals(expextedResult.get(0).getId(), actualResult.get(0).getParentId().getId());
		assertEquals(expextedResult.get(0).getName(), actualResult.get(0).getParentId().getName());
	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_related_data_when_parentid_is_wrong() {
		List<Function> expextedResult = new ArrayList<>();
		expextedResult.add(getParentFunction());
		List<Function> functions = new ArrayList<>();
		functions.add(getFunction());
		when(functionRepository.findById(any(Function.class))).thenReturn(null);
		functionService.fetchRelated(functions);
	}

	private Function getFunction() {
		Function function = Function.builder().id("1").name("function").code("001").active(true)
				.parentId(getParentFunction()).level(1).isParent(false).build();
		return function;
	}

	private Function getParentFunction() {
		Function function = Function.builder().id("2").name("functionParent").code("002").active(true).parentId(null)
				.level(1).isParent(false).build();
		return function;
	}

	private FunctionSearch getFunctionSearch() {
		FunctionSearch functionSearch = new FunctionSearch();
		functionSearch.setPageSize(500);
		functionSearch.setOffset(0);
		functionSearch.setSortBy("name desc");
		return functionSearch;

	}

	private FunctionContract getFunctionContract() {
		return FunctionContract.builder().code("001").name("function").active(true).level(1).isParent(false).build();
	}

}
