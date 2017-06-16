/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssetService {

	private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AssetProducer assetProducer;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ObjectMapper objectMapper;

	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		logger.info("AssetService getAssets");
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		return getAssetResponse(assets, requestInfo);
	}

	public String getAssetName(final String tenantId, final String name) {
		logger.info("AssetService getAssetName");
		return assetRepository.findAssetName(tenantId, name);
	}

	public AssetResponse create(final AssetRequest assetRequest) {
		final Asset asset = assetRepository.create(assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse createAsync(final AssetRequest assetRequest) {

		assetRequest.getAsset().setCode(assetRepository.getAssetCode());
		assetRequest.getAsset().setId(Long.valueOf(assetRepository.getNextAssetId().longValue()));

		// TODO validate assetcategory for an asset
		logger.info("assetRequest createAsync::" + assetRequest);
		String value = null;

		try {
			value = objectMapper.writeValueAsString(assetRequest);
		} catch (final JsonProcessingException e) {
			logger.info("JsonProcessingException assetrequest for kafka : " + e);
		}
		logger.info("assetRequest value::" + value);

		assetProducer.sendMessage(applicationProperties.getCreateAssetTopicName(), "save-asset", value);

		final List<Asset> assets = new ArrayList<>();
		assets.add(assetRequest.getAsset());
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse update(final AssetRequest assetRequest) {

		final Asset asset = assetRepository.update(assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {

		logger.info("assetRequest createAsync::" + assetRequest);
		String value = null;

		try {
			value = objectMapper.writeValueAsString(assetRequest);
		} catch (final JsonProcessingException e) {
			logger.info("JsonProcessingException assetrequest for update for kafka : " + e);
		}

		assetProducer.sendMessage(applicationProperties.getUpdateAssetTopicName(), "update-asset", value);

		final List<Asset> assets = new ArrayList<>();
		assets.add(assetRequest.getAsset());
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	private AssetResponse getAssetResponse(final List<Asset> assets, final RequestInfo requestInfo) {
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetResponse;
	}
}