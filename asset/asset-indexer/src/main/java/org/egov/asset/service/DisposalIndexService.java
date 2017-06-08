package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalIndex;
import org.egov.asset.repository.DisposalIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DisposalIndexService {
	public static final Logger LOGGER = LoggerFactory.getLogger(DisposalIndexService.class);

	@Autowired
	private DisposalIndexRepository disposalIndexRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public void postAssetDisposal(final DisposalRequest disposalRequest) {
		if (disposalRequest != null) {
			final DisposalIndex disposalIndex = prepareRevaluationIndex(disposalRequest);
			LOGGER.info("the logged value of disposalIndex ::" + disposalIndex);
			disposalIndexRepository.saveOrUpdateAssetDisposal(disposalIndex);
		} else
			LOGGER.info("DisposalRequest object is null");
	}

	private DisposalIndex prepareRevaluationIndex(final DisposalRequest disposalRequest) {
		final DisposalIndex disposalIndex = new DisposalIndex();
		final Disposal disposal = disposalRequest.getDisposal();
		disposalIndex.setDisposalData(disposal);
		setAssetData(disposalIndex, disposal);
		setAuditDetails(disposalIndex, disposal);
		return disposalIndex;
	}

	private void setAssetData(final DisposalIndex disposalIndex, final Disposal disposal) {
		final Asset asset = getAssetData(disposal.getAssetId(), disposal.getTenantId());
		if (asset != null) {
			disposalIndex.setAssetId(asset.getId());
			disposalIndex.setAssetCode(asset.getCode());
			disposalIndex.setAssetName(asset.getName());
		}
	}

	private Asset getAssetData(final Long assetId, final String tenantId) {
		final String url = applicationProperties.getAssetServiceHostName()
				+ applicationProperties.getAssetServiceSearchPath() + "?&tenantId=" + tenantId + "&id="
				+ assetId.toString();
		LOGGER.info("asset search url :: " + url);
		final Asset asset = restTemplate.postForObject(url, new RequestInfo(), Asset.class);
		LOGGER.info("asset object :: " + asset);

		return asset;
	}

	private void setAuditDetails(final DisposalIndex disposalIndex, final Disposal disposal) {
		final AuditDetails ad = disposal.getAuditDetails();
		if (ad != null) {
			disposalIndex.setCreatedBy(ad.getCreatedBy());
			disposalIndex.setCreatedDate(ad.getCreatedDate());
			disposalIndex.setLastModifiedBy(ad.getLastModifiedBy());
			disposalIndex.setLastModifiedDate(ad.getLastModifiedDate());
		}
	}
}
