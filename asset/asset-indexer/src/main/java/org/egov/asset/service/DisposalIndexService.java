package org.egov.asset.service;

import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalIndex;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.DisposalIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisposalIndexService {
	public static final Logger LOGGER = LoggerFactory.getLogger(DisposalIndexService.class);

	@Autowired
	private DisposalIndexRepository disposalIndexRepository;

	@Autowired
	private AssetIndexCommonService assetIndexCommonService;

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
		setTenantProperties(disposalIndex, disposal.getTenantId());
		return disposalIndex;
	}

	public void setAssetData(final DisposalIndex disposalIndex, final Disposal disposal) {
		final Asset asset = assetIndexCommonService.getAssetData(disposal.getAssetId(), disposal.getTenantId());
		if (asset != null) {
			disposalIndex.setAssetId(asset.getId());
			disposalIndex.setAssetCode(asset.getCode());
			disposalIndex.setAssetName(asset.getName());
		}
	}

	public void setAuditDetails(final DisposalIndex disposalIndex, final Disposal disposal) {
		final AuditDetails ad = disposal.getAuditDetails();
		if (ad != null) {
			disposalIndex.setCreatedBy(ad.getCreatedBy());
			disposalIndex.setCreatedDate(ad.getCreatedDate());
			disposalIndex.setLastModifiedBy(ad.getLastModifiedBy());
			disposalIndex.setLastModifiedDate(ad.getLastModifiedDate());
		}
	}

	private void setTenantProperties(final DisposalIndex disposalIndex, final String code) {
		final Tenant tenant = assetIndexCommonService.getTenantData(code).get(0);
		disposalIndex.setCityName(tenant.getCity().getName());
		disposalIndex.setUlbGrade(tenant.getCity().getUlbGrade());
		disposalIndex.setLocalName(tenant.getCity().getLocalName());
		disposalIndex.setDistrictCode(tenant.getCity().getDistrictCode());
		disposalIndex.setDistrictName(tenant.getCity().getDistrictName());
		disposalIndex.setRegionName(tenant.getCity().getRegionName());
	}

}
