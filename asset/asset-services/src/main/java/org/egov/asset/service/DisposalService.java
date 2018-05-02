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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.repository.DisposalRepository;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisposalService {

    @Autowired
    private DisposalRepository disposalRepository;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private CurrentValueService currentValueService;

    public List<Disposal> search(final DisposalCriteria disposalCriteria, final RequestInfo requestInfo) {
        List<Disposal> disposals = null;

        try {
            disposals = disposalRepository.search(disposalCriteria);
        } catch (final Exception ex) {
            log.info("DisposalService:", ex);
            throw new RuntimeException(ex);
        }
        return disposals;
    }

    public void create(final DisposalRequest disposalRequest) {
        disposalRepository.create(disposalRequest);
        setStatusOfAssetToDisposed(disposalRequest);
    }

    public void setStatusOfAssetToDisposed(final DisposalRequest disposalRequest) {
        final Disposal disposal = disposalRequest.getDisposal();
        final String tenantId = disposal.getTenantId();
        final Asset asset = assetService.getAsset(tenantId, disposal.getAssetId(), disposalRequest.getRequestInfo());
        final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,
                Status.DISPOSED, tenantId);
        asset.setStatus(assetStatuses.get(0).getStatusValues().get(0).getCode());
        final AssetRequest assetRequest = AssetRequest.builder().asset(asset)
                .requestInfo(disposalRequest.getRequestInfo()).build();
        assetService.update(assetRequest);
    }

    public Disposal saveDisposal(final DisposalRequest disposalRequest, final HttpHeaders headers) {
        final Disposal disposal = disposalRequest.getDisposal();

        disposal.setId(assetCommonService.getNextId(Sequence.DISPOSALSEQUENCE));

        if (disposal.getAuditDetails() == null)
            disposal.setAuditDetails(assetCommonService.getAuditDetails(disposalRequest.getRequestInfo()));
        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                disposal.getTenantId()))
            try {
                log.info("Commencing Voucher Generation for Asset Sale/Disposal");
                final String voucherNumber = createVoucherForDisposal(disposalRequest, headers);
                if (StringUtils.isNotBlank(voucherNumber))
                    disposal.setProfitLossVoucherReference(voucherNumber);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }

        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(disposal.getAssetId());
        assetCurrentValue.setAssetTranType(disposal.getTransactionType());
        assetCurrentValue.setCurrentAmount(disposal.getSaleValue() != null ? disposal.getSaleValue() : BigDecimal.ZERO);
        assetCurrentValue.setTenantId(disposal.getTenantId());
        assetCurrentValues.add(assetCurrentValue);
        final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
        assetCurrentValueRequest.setRequestInfo(disposalRequest.getRequestInfo());
        assetCurrentValueRequest.setAssetCurrentValues(assetCurrentValues);
        currentValueService.createCurrentValue(assetCurrentValueRequest);
        create(disposalRequest);
        return disposal;
    }

    public String createVoucherForDisposal(final DisposalRequest disposalRequest, final HttpHeaders headers) {
        final Disposal disposal = disposalRequest.getDisposal();
        final RequestInfo requestInfo = disposalRequest.getRequestInfo();
        final List<Long> assetIds = new ArrayList<>();
        final String tenantId = disposal.getTenantId();
        assetIds.add(disposal.getAssetId());
        final Asset asset = assetRepository
                .findForCriteria(AssetCriteria.builder().tenantId(tenantId).id(assetIds).build()).get(0);

        final AssetCategory assetCategory = asset.getAssetCategory();

        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetSaleAccount = voucherService
                .getSubledgerDetails(requestInfo, tenantId, disposal.getAssetSaleAccount());
        voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount, subledgerDetailsForAssetSaleAccount);

        final List<VoucherAccountCodeDetails> accountCodeDetails = getAccountDetails(disposal, assetCategory,
                requestInfo);
        log.debug("Voucher Create Account Code Details :: " + accountCodeDetails);

        final VoucherRequest voucherRequest = voucherService.createDisposalVoucherRequest(disposal, asset.getId(),
                asset.getDepartment().getId(), accountCodeDetails, asset.getFunction(), headers);

        log.debug("Voucher Request for Disposal :: " + voucherRequest);

        return voucherService.createVoucher(voucherRequest, tenantId, headers);

    }

    private List<VoucherAccountCodeDetails> getAccountDetails(final Disposal disposal,
            final AssetCategory assetCategory, final RequestInfo requestInfo) {
        final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<>();
        final String tenantId = disposal.getTenantId();
        if(disposal.getTransactionType().equals(TransactionType.SALE)) {
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, disposal.getAssetSaleAccount(),
                disposal.getSaleValue(), false, true));
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                disposal.getSaleValue(), true, false));
        }else
            if ( disposal.getTransactionType().equals(TransactionType.DISPOSAL)){
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, disposal.getAssetSaleAccount(),
                    disposal.getAssetCurrentValue(), false, true));
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                    disposal.getAssetCurrentValue(), true, false));
            }
        return accountCodeDetails;
    }

}
