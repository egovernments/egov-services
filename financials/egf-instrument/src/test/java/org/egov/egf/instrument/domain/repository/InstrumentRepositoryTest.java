package org.egov.egf.instrument.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.instrument.persistence.entity.InstrumentEntity;
import org.egov.egf.instrument.persistence.repository.InstrumentJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentRepositoryTest {

	@InjectMocks
	private InstrumentRepository instrumentRepository;

	@Mock
	private InstrumentJdbcRepository instrumentJdbcRepository;

	@Test
	public void test_find_by_id() {
		InstrumentEntity entity = getInstrumentEntity();
		Instrument expectedResult = entity.toDomain();

		when(instrumentJdbcRepository.findById(any(InstrumentEntity.class))).thenReturn(entity);

		Instrument actualResult = instrumentRepository.findById(getInstrumentDomin());

		assertEquals(expectedResult.getAmount(), actualResult.getAmount());
		assertEquals(expectedResult.getTransactionNumber(), actualResult.getTransactionNumber());
		assertEquals(expectedResult.getSerialNo(), actualResult.getSerialNo());
		assertEquals(expectedResult.getTransactionType(), actualResult.getTransactionType());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());
	}

	@Test
	public void test_find_by_id_return_null() {
		InstrumentEntity entity = getInstrumentEntity();

		when(instrumentJdbcRepository.findById(null)).thenReturn(entity);

		Instrument actualResult = instrumentRepository.findById(getInstrumentDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		InstrumentEntity entity = getInstrumentEntity();
		Instrument expectedResult = entity.toDomain();

		when(instrumentJdbcRepository.create(any(InstrumentEntity.class))).thenReturn(entity);

		Instrument actualResult = instrumentRepository.save(getInstrumentDomin());

		assertEquals(expectedResult.getAmount(), actualResult.getAmount());
		assertEquals(expectedResult.getTransactionNumber(), actualResult.getTransactionNumber());
		assertEquals(expectedResult.getSerialNo(), actualResult.getSerialNo());
		assertEquals(expectedResult.getTransactionType(), actualResult.getTransactionType());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());

	}

	@Test
	public void test_update() {

		InstrumentEntity entity = getInstrumentEntity();
		Instrument expectedResult = entity.toDomain();

		when(instrumentJdbcRepository.update(any(InstrumentEntity.class))).thenReturn(entity);

		Instrument actualResult = instrumentRepository.update(getInstrumentDomin());

		assertEquals(expectedResult.getAmount(), actualResult.getAmount());
		assertEquals(expectedResult.getTransactionNumber(), actualResult.getTransactionNumber());
		assertEquals(expectedResult.getSerialNo(), actualResult.getSerialNo());
		assertEquals(expectedResult.getTransactionType(), actualResult.getTransactionType());
		assertEquals(expectedResult.getInstrumentType().getId(), actualResult.getInstrumentType().getId());
	}

	@Test
	public void test_search() {

		Pagination<Instrument> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(instrumentJdbcRepository.search(any(InstrumentSearch.class))).thenReturn(expectedResult);

		Pagination<Instrument> actualResult = instrumentRepository.search(getInstrumentSearch());

		assertEquals(expectedResult, actualResult);

	}

	private Instrument getInstrumentDomin() {
		Instrument instrumentDetail = new Instrument();
		instrumentDetail.setAmount(BigDecimal.ONE);
		instrumentDetail.setTransactionNumber("transactionNumber");
		instrumentDetail.setSerialNo("serialNo");
		instrumentDetail.setTransactionType(TransactionType.Credit);
		instrumentDetail.setInstrumentType(InstrumentType.builder().id("instrumentTypeId").build());
		instrumentDetail.setTenantId("default");
		return instrumentDetail;
	}

	private InstrumentEntity getInstrumentEntity() {
		InstrumentEntity entity = new InstrumentEntity();
		entity.setAmount(BigDecimal.ONE);
		entity.setTransactionNumber("transactionNumber");
		entity.setSerialNo("serialNo");
		entity.setTransactionType(TransactionType.Credit.name());
		entity.setInstrumentTypeId("instrumentTypeId");
		entity.setTenantId("default");
		return entity;
	}

	private InstrumentSearch getInstrumentSearch() {
		InstrumentSearch instrumentSearch = new InstrumentSearch();
		instrumentSearch.setPageSize(500);
		instrumentSearch.setOffset(0);
		return instrumentSearch;

	}

}
