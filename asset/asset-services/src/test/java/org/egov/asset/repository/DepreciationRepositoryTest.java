package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.repository.builder.DepreciationQueryBuilder;
import org.egov.asset.repository.rowmapper.CalculationAssetDetailsRowMapper;
import org.egov.asset.repository.rowmapper.CalculationCurrentValueRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationDetailRowMapper;
import org.egov.asset.repository.rowmapper.DepreciationSumRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DepreciationRepositoryTest {
	
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private CalculationAssetDetailsRowMapper calculationAssetDetailsRowMapper;

	@Mock
	private CalculationCurrentValueRowMapper calculationCurrentValueRowMapper;

	@Mock
	private DepreciationQueryBuilder depreciationQueryBuilder;
	
	@Mock
	private DepreciationDetailRowMapper depreciationDetailRowMapper;
	
	@Mock
	private DepreciationSumRowMapper depreciationSumRowMapper;
	
	@Mock
	private AssetConfigurationService assetConfigurationService;
	
	@InjectMocks
	private DepreciationRepository depreciationRepository;
	
	@Test
	@PrepareForTest({DepreciationQueryBuilder.class})
	public void test_get_depreciation_details(){
		
		ArrayList<DepreciationDetail> depreciationDetails = new ArrayList<>();
		String s = "query";

		PowerMockito.when(depreciationQueryBuilder.getDepreciationSearchQuery(any(DepreciationCriteria.class), any(List.class))).thenReturn(s);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(DepreciationDetailRowMapper.class)))
		.thenReturn(depreciationDetails);
		
		assertTrue(depreciationDetails.equals(depreciationRepository.getDepreciationdetails(new DepreciationCriteria())));
		
	}
	
}
