/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceTest {

	@Mock
	private AssetRepository assetRepository;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private AssetService assetService;

	@Mock
	private ResponseInfoFactory responseInfoFactory;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Mock
	private AssetCommonService assetCommonService;
	
	@Mock
	 private CurrentValueService currentValueService;

	@Test
	public void testSearch() {
		final List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);

		final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId("ap.kurnool").build();
		when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
		assertEquals(assetResponse, assetService.getAssets(assetCriteria, new RequestInfo()));
	}

	@Test
	public void testCreate() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.create(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.create(assetRequest)));
	}

	@Test
	public void testCreateAsync() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetCommonService.getDepreciationRate(any(Double.class))).thenReturn(Double.valueOf("13.17"));

		assertTrue(assetResponse.equals(assetService.createAsync(assetRequest)));
	}

	@Test
	public void testUpdate() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.update(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.update(assetRequest)));
	}

	/*@Test
	public void testUpdateAsync() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);
		final AssetResponse assetResponse = getAssetResponse(asset);
		final Set<Long> assetIds = new HashSet<Long>();
		assetIds.add(asset.getId());
                //currentValueService.getCurrentValues(assetIds, "ap.kurnool", new RequestInfo());

		when(assetCommonService.getDepreciationRate(any(Double.class))).thenReturn(Double.valueOf("13.17"));
		when(currentValueService.getCurrentValues(assetIds, "ap.kurnool", new RequestInfo()));
		assertTrue(assetResponse.equals(assetService.updateAsync(assetRequest)));
	}*/

	@Test
	public void testGetAsset() {

		final Asset expectedAsset = getAsset();
		final List<Asset> assets = new ArrayList<>();
		assets.add(expectedAsset);
		when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
		final Asset actualAsset = assetService.getAsset("ap.kurnool", Long.valueOf("552"), new RequestInfo());

		assertEquals(expectedAsset, actualAsset);

	}
/*
	@Test
	public void testGetDepreciationReport() {
		final List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		final AssetResponse expectedAssetResponse = new AssetResponse();
		expectedAssetResponse.setAssets(assets);

		when(assetRepository.getDepreciatedAsset(any(DepreciationReportCriteria.class))).thenReturn(assets);

		final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
				.assetCategoryType(AssetCategoryType.IMMOVABLE.toString()).assetCategoryName("category name")
				.tenantId("ap.kurnool").build();
		final AssetResponse actualAssetResponse = assetService.getDepreciationReport(new RequestInfo(),
				depreciationReportCriteria);

		assertEquals(expectedAssetResponse, actualAssetResponse);

	}*/

	private AssetResponse getAssetResponse(final Asset asset) {
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);
		return assetResponse;
	}

	private Asset getAsset() {
		final Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setId(Long.valueOf("5"));
		asset.setName("asset name");
		asset.setStatus(Status.CREATED.toString());
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
		asset.setEnableYearWiseDepreciation(false);
		asset.setDepreciationRate(Double.valueOf("13.17"));
		asset.setFunction("020");

		final Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");

		final AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(1l);
		assetCategory.setName("category name");
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setTenantId("ap.kurnool");

		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		return asset;
	}


}
