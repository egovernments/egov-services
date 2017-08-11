package org.egov.asset.service;

import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalIndex;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.DisposalIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisposalIndexService {

    @Autowired
    private DisposalIndexRepository disposalIndexRepository;

    @Autowired
    private AssetIndexCommonService assetIndexCommonService;

    public void postAssetDisposal(final DisposalRequest disposalRequest) {
        if (disposalRequest != null) {
            final DisposalIndex disposalIndex = prepareDisposalIndex(disposalRequest);
            log.info("the logged value of disposalIndex ::" + disposalIndex);
            disposalIndexRepository.saveAssetDisposal(disposalIndex);
        } else
            log.info("DisposalRequest object is null");
    }

    private DisposalIndex prepareDisposalIndex(final DisposalRequest disposalRequest) {
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

    private void setTenantProperties(final DisposalIndex disposalIndex, final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(tenantId).get(0);
        disposalIndex.setCityName(tenant.getCity().getName());
        disposalIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        disposalIndex.setLocalName(tenant.getCity().getLocalName());
        disposalIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        disposalIndex.setDistrictName(tenant.getCity().getDistrictName());
        disposalIndex.setRegionName(tenant.getCity().getRegionName());
    }

}
