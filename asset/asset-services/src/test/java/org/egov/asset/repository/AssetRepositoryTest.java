package org.egov.asset.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.builder.AssetQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssetRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private AssetRowMapper assetRowMapper;

	@Mock
	private AssetQueryBuilder assetQueryBuilder;

	@InjectMocks
	private AssetRepository assetRepository;
	
	@Mock
	private ObjectMapper objectMapper;

	@Test
	public void testFindForCriteria() {

		List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		String query = "";
		when(assetQueryBuilder.getQuery(any(AssetCriteria.class), any(List.class))).thenReturn(query);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(AssetRowMapper.class))).thenReturn(assets);

		assertTrue(assets.equals(assetRepository.findForCriteria(new AssetCriteria())));
	}

/*	@Test
	public void testGetAssetCode() {

		String query = "0000001";
		when(jdbcTemplate.queryForObject(any(String.class),any(RowMapper.class))).thenReturn(1);
		
		assertTrue(query.equals(assetRepository.getAssetCode()));
	}*/

	@Test
	public void testCreateAsset() {
		
		AssetRequest assetRequest = new AssetRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		assetRequest.setRequestInfo(requestInfo);
		Asset asset = getAsset();
		assetRequest.setAsset(asset);
		
		when(jdbcTemplate.update(any(String.class),any(Object[].class))).thenReturn(1);
		assertTrue(asset.equals(assetRepository.create(assetRequest)));
	}
	
	@Test
	public void testUpdateAsset() {
		
		AssetRequest assetRequest = new AssetRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		assetRequest.setRequestInfo(requestInfo);
		Asset asset = getAsset();
		assetRequest.setAsset(asset);
		
		when(jdbcTemplate.update(any(String.class),any(Object[].class))).thenReturn(1);
		assertTrue(asset.equals(assetRepository.update(assetRequest)));
	}

	private Asset getAsset() {
		Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setName("asset name");
		asset.setStatus(Status.CREATED);
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);

		Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");

		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(1l);
		assetCategory.setName("category name");

		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		
		Department department = new Department();
		asset.setDepartment(department);
		
		return asset;
	}

}
