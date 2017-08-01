package org.egov.egf.instrument.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.TestConfiguration;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.repository.InstrumentRepository;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
import org.egov.egf.instrument.domain.repository.SurrenderReasonRepository;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.repository.BankAccountContractRepository;
import org.egov.egf.master.web.repository.BankContractRepository;
import org.egov.egf.master.web.repository.FinancialStatusContractRepository;
import org.junit.Before;
import org.junit.Ignore;
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
public class InstrumentServiceTest {

	private InstrumentService instrumentService;

	@Mock
	private InstrumentRepository instrumentRepository;

	@Mock
	private SmartValidator validator;

	@Mock
	private SurrenderReasonRepository surrenderReasonRepository;

	@Mock
	private BankContractRepository bankContractRepository;

	@Mock
	private FinancialStatusContractRepository financialStatusContractRepository;

	@Mock
	private BankAccountContractRepository bankAccountContractRepository;

	@Mock
	private InstrumentTypeRepository instrumentTypeRepository;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	@Before
	public void setup() {
		instrumentService = new InstrumentService(validator, instrumentRepository, surrenderReasonRepository,
				bankContractRepository, financialStatusContractRepository, bankAccountContractRepository,
				instrumentTypeRepository);
	}

	@Test
	public final void test_save_with_out_kafka() {

		List<Instrument> expextedResult = getInstruments();

		Instrument instrument = expextedResult.get(0);

		when(instrumentRepository.save(any(Instrument.class))).thenReturn(instrument);

		List<Instrument> actualResult = instrumentService.save(expextedResult, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_save_with_out_kafka_and_with_null_req() {

		List<Instrument> expextedResult = getInstruments();

		Instrument instrument = expextedResult.get(0);

		when(instrumentRepository.save(any(Instrument.class))).thenReturn(instrument);

		List<Instrument> actualResult = instrumentService.save(null, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update_with_out_kafka() {

		List<Instrument> expextedResult = getInstruments();

		Instrument instrument = expextedResult.get(0);

		when(instrumentRepository.update(any(Instrument.class))).thenReturn(instrument);

		List<Instrument> actualResult = instrumentService.update(expextedResult, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_update_with_out_kafka_and_with_null_req() {

		List<Instrument> expextedResult = getInstruments();

		Instrument instrument = expextedResult.get(0);

		when(instrumentRepository.update(any(Instrument.class))).thenReturn(instrument);

		List<Instrument> actualResult = instrumentService.update(null, errors);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_create() {

		List<Instrument> expextedResult = getInstruments();

		List<Instrument> actualResult = instrumentService.fetchAndValidate(expextedResult, errors, "create");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_update() {

		List<Instrument> expextedResult = getInstruments();

		List<Instrument> actualResult = instrumentService.fetchAndValidate(expextedResult, errors, "update");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_search() {

		List<Instrument> instruments = getInstruments();
		InstrumentSearch instrumentSearch = new InstrumentSearch();
		Pagination<Instrument> expextedResult = new Pagination<>();

		expextedResult.setPagedData(instruments);

		when(instrumentRepository.search(instrumentSearch)).thenReturn(expextedResult);

		Pagination<Instrument> actualResult = instrumentService.search(instrumentSearch);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_save() {

		Instrument expextedResult = getInstruments().get(0);

		when(instrumentRepository.save(any(Instrument.class))).thenReturn(expextedResult);

		Instrument actualResult = instrumentService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {

		Instrument expextedResult = getInstruments().get(0);

		when(instrumentRepository.update(any(Instrument.class))).thenReturn(expextedResult);

		Instrument actualResult = instrumentService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_fetch_instrumenttype() {

		List<Instrument> instruments = getInstruments();

		InstrumentType expextedResult = InstrumentType.builder().name("name").description("description").active(true)
				.id("1").build();

		instruments.get(0).setInstrumentType(expextedResult);

		when(instrumentTypeRepository.findById(any(InstrumentType.class))).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getInstrumentType());
	}

	@Test
	public final void test_fetch_bank() {

		List<Instrument> instruments = getInstruments();

		BankContract expextedResult = BankContract.builder().name("name").description("description").active(true)
				.id("1").build();

		instruments.get(0).setBank(expextedResult);

		when(bankContractRepository.findById(any(BankContract.class))).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getBank());
	}

	@Test
	public final void test_fetch_bankaccount() {

		List<Instrument> instruments = getInstruments();

		BankAccountContract expextedResult = BankAccountContract.builder().accountNumber("accountNumber")
				.description("description").active(true).id("1").build();

		instruments.get(0).setBankAccount(expextedResult);

		when(bankAccountContractRepository.findById(any(BankAccountContract.class))).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getBankAccount());
	}

	@Test
	public final void test_fetch_financialstatus() {

		List<Instrument> instruments = getInstruments();

		FinancialStatusContract expextedResult = FinancialStatusContract.builder().name("name")
				.description("description").id("1").build();

		instruments.get(0).setFinancialStatus(expextedResult);

		when(financialStatusContractRepository.findById(any(FinancialStatusContract.class))).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getFinancialStatus());
	}

	@Test
	public final void test_fetch_surrenderreason() {

		List<Instrument> instruments = getInstruments();

		SurrenderReason expextedResult = SurrenderReason.builder().name("name").description("description").id("1")
				.build();

		instruments.get(0).setSurrenderReason(expextedResult);

		when(surrenderReasonRepository.findById(any(SurrenderReason.class))).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getSurrenderReason());
	}
	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_instrumenttype_null() {

		List<Instrument> instruments = getInstruments();

		InstrumentType expextedResult = InstrumentType.builder().name("name").description("description").active(true)
				.id("1").build();

		instruments.get(0).setInstrumentType(expextedResult);

		when(instrumentTypeRepository.findById(null)).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getInstrumentType());
	}
	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_bank_null() {

		List<Instrument> instruments = getInstruments();

		BankContract expextedResult = BankContract.builder().name("name").description("description").active(true)
				.id("1").build();

		instruments.get(0).setBank(expextedResult);

		when(bankContractRepository.findById(null)).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getBank());
	}

	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_bankaccount_null() {

		List<Instrument> instruments = getInstruments();

		BankAccountContract expextedResult = BankAccountContract.builder().accountNumber("accountNumber")
				.description("description").active(true).id("1").build();

		instruments.get(0).setBankAccount(expextedResult);

		when(bankAccountContractRepository.findById(null)).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getBankAccount());
	}

	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_financialstatus_null() {

		List<Instrument> instruments = getInstruments();

		FinancialStatusContract expextedResult = FinancialStatusContract.builder().name("name")
				.description("description").id("1").build();

		instruments.get(0).setFinancialStatus(expextedResult);

		when(financialStatusContractRepository.findById(null)).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getFinancialStatus());
	}
	@Ignore
	@Test(expected = InvalidDataException.class)
	public final void test_fetch_surrenderreason_null() {

		List<Instrument> instruments = getInstruments();

		SurrenderReason expextedResult = SurrenderReason.builder().name("name").description("description").id("1")
				.build();

		instruments.get(0).setSurrenderReason(expextedResult);

		when(surrenderReasonRepository.findById(null)).thenReturn(expextedResult);

		List<Instrument> actualResult = instrumentService.fetchRelated(instruments);

		assertEquals(expextedResult, actualResult.get(0).getSurrenderReason());
	}

	private List<Instrument> getInstruments() {
		List<Instrument> instruments = new ArrayList<Instrument>();
		Instrument instrument = Instrument.builder().build();
		instrument.setTenantId("default");
		instruments.add(instrument);
		return instruments;
	}

}
