package org.egov.asset.service;

import java.util.Map;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetIndex;
import org.egov.asset.model.Boundary;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.Tenant;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.repository.AssetIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetIndexService {

    @Autowired
    private AssetIndexRepository assetRepository;

    @Autowired
    private AssetIndexCommonService assetIndexCommonService;

    @Autowired
    private ObjectMapper mapper;

    public void postAsset(final AssetRequest assetRequest) {
        final AssetIndex assetIndex = prepareAssetIndex(assetRequest);
        log.info("the logged value of assetIndex ::" + assetIndex);
        assetRepository.saveAsset(assetIndex);
    }

    public AssetIndex prepareAssetIndex(final AssetRequest assetRequest) {
        final AssetIndex assetIndex = new AssetIndex();
        final Asset asset = assetRequest.getAsset();
        final Location location = asset.getLocationDetails();
        setAssetData(assetIndex, asset);
        final Map<Long, Boundary> locationMap = assetRepository.getlocationsById(asset);
        setAssetLocation(assetIndex, location, locationMap);
        setTenantProperties(assetRequest.getRequestInfo(), assetIndex, asset.getTenantId());
        return assetIndex;
    }

    private void setAssetData(final AssetIndex assetIndex, final Asset asset) {
        assetIndex.setTenantId(asset.getTenantId());
        assetIndex.setAssetId(asset.getId());
        assetIndex.setAssetName(asset.getName());
        assetIndex.setAssetCode(asset.getCode());
        assetIndex.setGrossValue(asset.getGrossValue());
        assetIndex.setAccumulatedDepreciation(asset.getAccumulatedDepreciation());
        assetIndex.setAssetReference(asset.getAssetReference());
        setAssetCategory(assetIndex, asset);
        if (asset.getModeOfAcquisition() != null)
            assetIndex.setModeOfAcquisition(asset.getModeOfAcquisition().toString());
        assetIndex.setStatus(asset.getStatus());
        assetIndex.setAssetDetails(asset.getAssetDetails());
        assetIndex.setDescription(asset.getDescription());
        assetIndex.setDateOfCreation(asset.getDateOfCreation());
        assetIndex.setRemarks(asset.getRemarks());
        assetIndex.setLength(asset.getLength());
        assetIndex.setWidth(asset.getWidth());
        assetIndex.setTotalArea(asset.getTotalArea());
        mapper.setSerializationInclusion(Include.NON_NULL);
        try {
            assetIndex.setAssetAttributes(mapper.writeValueAsString(asset.getAssetAttributes()));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        setAssetDepartment(assetIndex, asset.getDepartment());
        assetIndex.setEnableYearWiseDepreciation(asset.getEnableYearWiseDepreciation());
        assetIndex.setDepreciationRate(asset.getDepreciationRate());
        try {
            assetIndex.setYearWiseDepreciation(mapper.writeValueAsString(asset.getYearWiseDepreciation()));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void setAssetCategory(final AssetIndex assetIndex, final Asset asset) {
        final AssetCategory assetCategory = asset.getAssetCategory();
        assetIndex.setAssetCategoryId(assetCategory.getId());
        assetIndex.setAssetCategoryName(assetCategory.getName());
        assetIndex.setAssetCategoryCode(assetCategory.getCode());
        assetIndex.setAssetCategoryparentId(assetCategory.getParent());
        assetIndex.setAssetCategoryDepreciationRate(assetCategory.getDepreciationRate());
        final DepreciationMethod depreciationMethod = assetCategory.getDepreciationMethod();
        if (depreciationMethod != null)
            assetIndex.setDepreciationMethod(depreciationMethod.toString());
    }

    private void setAssetDepartment(final AssetIndex assetIndex, final Department department) {
        assetIndex.setDepartmentId(department.getId());
        assetIndex.setDepartmentName(department.getName());
        assetIndex.setDepartmentCode(department.getCode());
    }

    private void setAssetLocation(final AssetIndex assetIndex, final Location location,
            final Map<Long, Boundary> locationMap) {
        assetIndex.setLocality(location.getLocality());
        final Boundary locationBoundary = locationMap.get(location.getLocality());
        if (locationBoundary != null)
            assetIndex.setLocalityName(locationBoundary.getName());

        assetIndex.setZone(location.getZone());
        final Boundary zoneBoundary = locationMap.get(location.getZone());
        if (zoneBoundary != null)
            assetIndex.setZoneName(zoneBoundary.getName());

        assetIndex.setRevenueWard(location.getRevenueWard());
        final Boundary revenueWardBoundary = locationMap.get(location.getRevenueWard());
        if (revenueWardBoundary != null)
            assetIndex.setRevenueWardName(revenueWardBoundary.getName());

        assetIndex.setBlock(location.getBlock());
        final Boundary blockBoundary = locationMap.get(location.getBlock());
        if (blockBoundary != null)
            assetIndex.setBlockName(blockBoundary.getName());

        assetIndex.setStreet(location.getStreet());
        final Boundary streetBoundary = locationMap.get(location.getStreet());
        if (streetBoundary != null)
            assetIndex.setStreetName(streetBoundary.getName());

        assetIndex.setElectionWard(location.getElectionWard());
        final Boundary electionWardBoundary = locationMap.get(location.getElectionWard());
        if (electionWardBoundary != null)
            assetIndex.setElectionWardName(electionWardBoundary.getName());

        assetIndex.setDoorNo(location.getDoorNo());
        assetIndex.setPinCode(location.getPinCode());
    }

    private void setTenantProperties(final RequestInfo requestInfo, final AssetIndex assetIndex,
            final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(requestInfo, tenantId).get(0);
        assetIndex.setCityName(tenant.getCity().getName());
        assetIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        assetIndex.setLocalName(tenant.getCity().getLocalName());
        assetIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        assetIndex.setDistrictName(tenant.getCity().getDistrictName());
        assetIndex.setRegionName(tenant.getCity().getRegionName());
    }

}
