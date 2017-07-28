package org.egov.egf.instrument.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.persistence.entity.SurrenderReasonEntity;
import org.egov.egf.instrument.persistence.repository.SurrenderReasonJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SurrenderReasonRepositoryTest {

	@InjectMocks
	private SurrenderReasonRepository surrenderReasonRepository;

	@Mock
	private SurrenderReasonJdbcRepository surrenderReasonJdbcRepository;

	@Test
	public void test_find_by_id() {
		SurrenderReasonEntity entity = getSurrenderReasonEntity();
		SurrenderReason expectedResult = entity.toDomain();

		when(surrenderReasonJdbcRepository.findById(any(SurrenderReasonEntity.class))).thenReturn(entity);

		SurrenderReason actualResult = surrenderReasonRepository.findById(getSurrenderReasonDomin());

		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}

	@Test
	public void test_find_by_id_return_null() {
		SurrenderReasonEntity entity = getSurrenderReasonEntity();

		when(surrenderReasonJdbcRepository.findById(null)).thenReturn(entity);

		SurrenderReason actualResult = surrenderReasonRepository.findById(getSurrenderReasonDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		SurrenderReasonEntity entity = getSurrenderReasonEntity();
		SurrenderReason expectedResult = entity.toDomain();

		when(surrenderReasonJdbcRepository.create(any(SurrenderReasonEntity.class))).thenReturn(entity);

		SurrenderReason actualResult = surrenderReasonRepository.save(getSurrenderReasonDomin());

		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());

	}

	@Test
	public void test_update() {

		SurrenderReasonEntity entity = getSurrenderReasonEntity();
		SurrenderReason expectedResult = entity.toDomain();

		when(surrenderReasonJdbcRepository.update(any(SurrenderReasonEntity.class))).thenReturn(entity);

		SurrenderReason actualResult = surrenderReasonRepository.update(getSurrenderReasonDomin());

		assertEquals(expectedResult.getName(), actualResult.getName());
		assertEquals(expectedResult.getDescription(), actualResult.getDescription());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}

	@Test
	public void test_search() {

		Pagination<SurrenderReason> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(surrenderReasonJdbcRepository.search(any(SurrenderReasonSearch.class))).thenReturn(expectedResult);

		Pagination<SurrenderReason> actualResult = surrenderReasonRepository.search(getSurrenderReasonSearch());

		assertEquals(expectedResult, actualResult);

	}

	private SurrenderReason getSurrenderReasonDomin() {
		SurrenderReason surrenderReasonDetail = new SurrenderReason();
		surrenderReasonDetail.setName("name");
		surrenderReasonDetail.setDescription("description");
		surrenderReasonDetail.setTenantId("default");
		return surrenderReasonDetail;
	}

	private SurrenderReasonEntity getSurrenderReasonEntity() {
		SurrenderReasonEntity entity = new SurrenderReasonEntity();
		entity.setName("name");
		entity.setDescription("description");
		entity.setTenantId("default");
		return entity;
	}

	private SurrenderReasonSearch getSurrenderReasonSearch() {
		SurrenderReasonSearch surrenderReasonSearch = new SurrenderReasonSearch();
		surrenderReasonSearch.setPageSize(500);
		surrenderReasonSearch.setOffset(0);
		return surrenderReasonSearch;

	}

}
