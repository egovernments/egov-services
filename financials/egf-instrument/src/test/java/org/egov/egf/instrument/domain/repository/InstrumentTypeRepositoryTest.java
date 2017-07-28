package org.egov.egf.instrument.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.persistence.entity.InstrumentTypeEntity;
import org.egov.egf.instrument.persistence.repository.InstrumentTypeJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentTypeRepositoryTest {

	@InjectMocks
	private InstrumentTypeRepository instrumentTypeRepository;

	@Mock
	private InstrumentTypeJdbcRepository instrumentTypeJdbcRepository;

	@Test
	public void test_find_by_id() {
		InstrumentTypeEntity entity = getInstrumentTypeEntity();
		InstrumentType expectedResult = entity.toDomain();

		when(instrumentTypeJdbcRepository.findById(any(InstrumentTypeEntity.class))).thenReturn(entity);

		InstrumentType actualResult = instrumentTypeRepository.findById(getInstrumentTypeDomin());

		assertEquals(expectedResult.getActive(), actualResult.getActive());
		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}

	@Test
	public void test_find_by_id_return_null() {
		InstrumentTypeEntity entity = getInstrumentTypeEntity();

		when(instrumentTypeJdbcRepository.findById(null)).thenReturn(entity);

		InstrumentType actualResult = instrumentTypeRepository.findById(getInstrumentTypeDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		InstrumentTypeEntity entity = getInstrumentTypeEntity();
		InstrumentType expectedResult = entity.toDomain();

		when(instrumentTypeJdbcRepository.create(any(InstrumentTypeEntity.class))).thenReturn(entity);

		InstrumentType actualResult = instrumentTypeRepository.save(getInstrumentTypeDomin());

		assertEquals(expectedResult.getActive(), actualResult.getActive());
		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());

	}

	@Test
	public void test_update() {

		InstrumentTypeEntity entity = getInstrumentTypeEntity();
		InstrumentType expectedResult = entity.toDomain();

		when(instrumentTypeJdbcRepository.update(any(InstrumentTypeEntity.class))).thenReturn(entity);

		InstrumentType actualResult = instrumentTypeRepository.update(getInstrumentTypeDomin());

		assertEquals(expectedResult.getActive(), actualResult.getActive());
		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}

	@Test
	public void test_search() {

		Pagination<InstrumentType> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(instrumentTypeJdbcRepository.search(any(InstrumentTypeSearch.class))).thenReturn(expectedResult);

		Pagination<InstrumentType> actualResult = instrumentTypeRepository.search(getInstrumentTypeSearch());

		assertEquals(expectedResult, actualResult);

	}

	private InstrumentType getInstrumentTypeDomin() {
		InstrumentType instrumentTypeDetail = new InstrumentType();
		instrumentTypeDetail.setActive(true);
		instrumentTypeDetail.setName("name");
		instrumentTypeDetail.setDescription("description");
		instrumentTypeDetail.setTenantId("default");
		return instrumentTypeDetail;
	}

	private InstrumentTypeEntity getInstrumentTypeEntity() {
		InstrumentTypeEntity entity = new InstrumentTypeEntity();
		entity.setActive(true);
		entity.setName("name");
		entity.setDescription("description");
		entity.setTenantId("default");
		return entity;
	}

	private InstrumentTypeSearch getInstrumentTypeSearch() {
		InstrumentTypeSearch instrumentTypeSearch = new InstrumentTypeSearch();
		instrumentTypeSearch.setPageSize(500);
		instrumentTypeSearch.setOffset(0);
		return instrumentTypeSearch;

	}

}
