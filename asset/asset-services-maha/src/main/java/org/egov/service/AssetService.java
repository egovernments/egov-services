package org.egov.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.model.Asset;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.repository.AssetRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private  AssetCommonService  assetCommonService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private AssetRepository assetRepository;

	public AssetResponse createAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();

		asset.setCode(assetCommonService.getCode("%06d", Sequence.ASSETCODESEQUENCE));

		asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));

		log.debug("assetRequest createAsync::" + assetRequest);
		asset.setAuditDetails(assetCommonService.getAuditDetails(assetRequest.getRequestInfo()));

		logAwareKafkaTemplate.send(applicationProperties.getCreateAssetTopicNameTemp(), assetRequest);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		log.debug("assetRequest updateAsync::" + assetRequest);
        logAwareKafkaTemplate.send(applicationProperties.getUpdateAssetTopicName(),
				KafkaTopicName.UPDATEASSET.toString(), assetRequest);
        final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}
	
	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		log.info("AssetService getAssets");
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		return getAssetResponse(assets, requestInfo);
	}

	private AssetResponse getAssetResponse(final List<Asset> assets, final RequestInfo requestInfo) {
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResposneInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetResponse;
	}

	public Asset getAsset(final String tenantId, final Long assetId, final RequestInfo requestInfo) {
		final List<Long> assetIds = new ArrayList<>();
		assetIds.add(assetId);
		final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId(tenantId).id(assetIds).build();
		final List<Asset> assets = getAssets(assetCriteria, requestInfo).getAssets();
		if (assets != null && !assets.isEmpty())
			return assets.get(0);
		else
			throw new RuntimeException(
					"There is no asset exists for id ::" + assetId + " for tenant id :: " + tenantId);
	}



}
