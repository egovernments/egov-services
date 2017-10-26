package org.egov.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.repository.AssetRepository;
import org.egov.repository.MasterDataRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class AssetService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private AssetCommonService assetCommonService;
	
	@Autowired
	private MasterDataRepository mDRepo;

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

		logAwareKafkaTemplate.send(appProps.getCreateAssetTopicNameTemp(), assetRequest);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		log.debug("assetRequest updateAsync::" + assetRequest);
		logAwareKafkaTemplate.send(appProps.getUpdateAssetTopicName(),
				KafkaTopicName.UPDATEASSET.toString(), assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		log.info("AssetService getAssets");
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		if(!assets.isEmpty())
		mapAssetCategories(assets, requestInfo);
		System.err.println(assets);
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

	private void mapAssetCategories(List<Asset> assets, RequestInfo requestInfo) {

		Set<Long> idSet = assets.stream().map(asset -> asset.getAssetCategory().getId()).collect(Collectors.toSet());
		System.err.println(idSet);
		Map<String, String> argsMap = new HashMap<>();
		argsMap.put(appProps.getMdMsMasterAssetCategory(), getIdQuery(idSet));

		JSONArray jsonArray = mDRepo.getAssetMastersById(argsMap, requestInfo, assets.get(0).getTenantId())
				.get(appProps.getMdMsMasterAssetCategory());
		List<AssetCategory> assetCategorys = new ArrayList<>();
		try {
			assetCategorys = Arrays
					.asList(new ObjectMapper().readValue(jsonArray.toJSONString(), AssetCategory[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Map<Long, AssetCategory> assetCatMap = assetCategorys.stream()
				.collect(Collectors.toMap(AssetCategory::getId, Function.identity()));
		assets.forEach(asset -> {
			Long key = asset.getAssetCategory().getId();
			asset.setAssetCategory(assetCatMap.get(key));
		});
	}

	private static String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}

}
