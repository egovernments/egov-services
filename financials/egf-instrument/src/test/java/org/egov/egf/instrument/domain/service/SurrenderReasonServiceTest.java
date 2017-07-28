package org.egov.egf.instrument.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.TestConfiguration;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.domain.repository.SurrenderReasonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class SurrenderReasonServiceTest {

	private SurrenderReasonService surrenderReasonService;

	@Mock
	private SmartValidator validator;

	@Mock
	private SurrenderReasonRepository surrenderReasonRepository;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	@Before
	public void setup() {
		surrenderReasonService = new SurrenderReasonService(validator, surrenderReasonRepository);
	}

	@Test
	public final void test_save_with_out_kafka() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		SurrenderReason surrenderReason = expextedResult.get(0);

		when(surrenderReasonRepository.save(any(SurrenderReason.class))).thenReturn(surrenderReason);

		List<SurrenderReason> actualResult = surrenderReasonService.save(expextedResult, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_save_with_out_kafka_and_with_null_req() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		SurrenderReason surrenderReason = expextedResult.get(0);

		when(surrenderReasonRepository.save(any(SurrenderReason.class))).thenReturn(surrenderReason);

		List<SurrenderReason> actualResult = surrenderReasonService.save(null, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update_with_out_kafka() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		SurrenderReason surrenderReason = expextedResult.get(0);

		when(surrenderReasonRepository.update(any(SurrenderReason.class))).thenReturn(surrenderReason);

		List<SurrenderReason> actualResult = surrenderReasonService.update(expextedResult, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_update_with_out_kafka_and_with_null_req() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		SurrenderReason surrenderReason = expextedResult.get(0);

		when(surrenderReasonRepository.update(any(SurrenderReason.class))).thenReturn(surrenderReason);

		List<SurrenderReason> actualResult = surrenderReasonService.update(null, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_create() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		List<SurrenderReason> actualResult = surrenderReasonService.fetchAndValidate(expextedResult, errors, "create");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_update() {

		List<SurrenderReason> expextedResult = getSurrenderReasons();

		List<SurrenderReason> actualResult = surrenderReasonService.fetchAndValidate(expextedResult, errors, "update");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_search() {

		List<SurrenderReason> surrenderReasons = getSurrenderReasons();
		SurrenderReasonSearch surrenderReasonSearch = new SurrenderReasonSearch();
		Pagination<SurrenderReason> expextedResult = new Pagination<>();

		expextedResult.setPagedData(surrenderReasons);

		when(surrenderReasonRepository.search(surrenderReasonSearch)).thenReturn(expextedResult);

		Pagination<SurrenderReason> actualResult = surrenderReasonService.search(surrenderReasonSearch);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_save() {

		SurrenderReason expextedResult = getSurrenderReasons().get(0);

		when(surrenderReasonRepository.save(any(SurrenderReason.class))).thenReturn(expextedResult);

		SurrenderReason actualResult = surrenderReasonService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {

		SurrenderReason expextedResult = getSurrenderReasons().get(0);

		when(surrenderReasonRepository.update(any(SurrenderReason.class))).thenReturn(expextedResult);

		SurrenderReason actualResult = surrenderReasonService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	private List<SurrenderReason> getSurrenderReasons() {
		List<SurrenderReason> surrenderReasons = new ArrayList<SurrenderReason>();
		SurrenderReason surrenderReason = SurrenderReason.builder().build();
		surrenderReason.setTenantId("default");
		surrenderReasons.add(surrenderReason);
		return surrenderReasons;
	}

}
