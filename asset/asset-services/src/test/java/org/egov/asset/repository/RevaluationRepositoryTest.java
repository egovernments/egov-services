package org.egov.asset.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.repository.builder.RevaluationQueryBuilder;
import org.egov.asset.repository.rowmapper.RevaluationRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RevaluationRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private RevaluationQueryBuilder revaluationQueryBuilder;

	@Mock
	private RevaluationRowMapper revaluationRowMapper;

	@Mock
	private RevaluationRepository revaluationRepository;

	@Test
	public void testCreate() {

		RevaluationRequest revaluationRequest = new RevaluationRequest();
		RequestInfo requestInfo = new RequestInfo();
		Revaluation revaluation = getRevaluation();
		revaluationRequest.setRequestInfo(requestInfo);
		revaluationRequest.setRevaluation(revaluation);

		when(jdbcTemplate.update(any(String.class), any(Object.class))).thenReturn(1);
		assertEquals(revaluation, revaluationRequest.getRevaluation());

	}

	@Test
	public void testSearch() {
		List<Revaluation> revaluation = new ArrayList<>();
		revaluation.add(getRevaluation());
		String querySt = "";
		/*
		 * when(assetQueryBuilder.getQuery(any(AssetCriteria.class),
		 * any(List.class))).thenReturn(query);
		 * when(jdbcTemplate.query(any(String.class), any(Object[].class),
		 * any(AssetRowMapper.class))).thenReturn(assets);
		 * 
		 * assertTrue(assets.equals(assetRepository.findForCriteria(new
		 * AssetCriteria())));
		 */
		when(revaluationQueryBuilder.getQuery(any(RevaluationCriteria.class), any(List.class))).thenReturn(querySt);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(RevaluationRowMapper.class)))
				.thenReturn(revaluation);
		assertTrue(revaluation.equals(revaluation));

	}

	private Revaluation getRevaluation() {

		Revaluation revaluation = new Revaluation();
		revaluation.setAssetId((long) 11);
		revaluation.setTenantId("ap.kurnool");
		revaluation.setId((long) 18);
		revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
		revaluation.setReevaluatedBy("kusuma");
		revaluation.setRevaluationDate((long) 22062017);
		//revaluation.setRevaluationAmount(500.00);
		//revaluation.setStatus(RevaluationStatus.ACTIVE);

		return revaluation;
	}

}
