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
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RevaluationService {

    @Autowired
    private RevaluationRepository revaluationRepository;

    @Autowired
    private AssetService assetService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private CurrentValueService currentValueService;

    public Revaluation saveRevaluation(final RevaluationRequest revaluationRequest, final HttpHeaders headers) {
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        log.debug("revaluationRequest:" + revaluationRequest);

        revaluation.setId(assetCommonService.getNextId(Sequence.REVALUATIONSEQUENCE));

        if (revaluation.getAuditDetails() == null)
            revaluation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));

        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                revaluation.getTenantId()))
            try {
                log.info("Commencing Voucher Generation for Asset Revaluation");
                final String voucherNumber = createVoucherForRevaluation(revaluationRequest, headers);

                if (StringUtils.isNotBlank(voucherNumber))
                    revaluation.setVoucherReference(voucherNumber);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }
        create(revaluationRequest);
        return revaluation;
    }

    public void create(final RevaluationRequest revaluationRequest) {
        revaluationRepository.create(revaluationRequest);
        saveRevaluationAmountToCurrentAmount(revaluationRequest);
    }

    public void saveRevaluationAmountToCurrentAmount(final RevaluationRequest revaluationRequest) {

        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(revaluation.getAssetId());
        assetCurrentValue.setAssetTranType(TransactionType.REVALUATION);
        assetCurrentValue.setCurrentAmount(revaluation.getValueAfterRevaluation());
        assetCurrentValue.setTenantId(revaluation.getTenantId());
        assetCurrentValues.add(assetCurrentValue);
        final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
        assetCurrentValueRequest.setRequestInfo(revaluationRequest.getRequestInfo());
        assetCurrentValueRequest.setAssetCurrentValues(assetCurrentValues);
        currentValueService.createCurrentValue(assetCurrentValueRequest);
    }

    public List<Revaluation> search(final RevaluationCriteria revaluationCriteria, final RequestInfo requestInfo) {
        List<Revaluation> revaluations = new ArrayList<>();
        try {
            revaluations = revaluationRepository.search(revaluationCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return revaluations;
    }

    public String createVoucherForRevaluation(final RevaluationRequest revaluationRequest, final HttpHeaders headers) {
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        final String tenantId = revaluation.getTenantId();
        final Asset asset = assetService.getAsset(tenantId, revaluation.getAssetId(), requestInfo);
        log.debug("asset for revaluation :: " + asset);

        final AssetCategory assetCategory = asset.getAssetCategory();

        if (revaluation.getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            log.info("subledger details check for Type of change INCREASED");

            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForRevaluationReserverAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getRevaluationReserveAccount());

            voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount,
                    subledgerDetailsForRevaluationReserverAccount);

        } else if (revaluation.getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            log.info("subledger details check for Type of change DECREASED");

            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForFixedAssetWrittenOffAccount = voucherService
                    .getSubledgerDetails(requestInfo, tenantId, revaluation.getFixedAssetsWrittenOffAccount());

            voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount,
                    subledgerDetailsForFixedAssetWrittenOffAccount);

        }

        final List<VoucherAccountCodeDetails> accountCodeDetails = getAccountDetails(revaluation, assetCategory,
                requestInfo);

        log.debug("Voucher Create Account Code Details :: " + accountCodeDetails);
        final VoucherRequest voucherRequest = voucherService.createRevaluationVoucherRequest(revaluation,
                accountCodeDetails, asset.getId(), asset.getDepartment().getId(), headers);
        log.debug("Voucher Request for Revaluation :: " + voucherRequest);

        return voucherService.createVoucher(voucherRequest, tenantId, headers);

    }

    public List<VoucherAccountCodeDetails> getAccountDetails(final Revaluation revaluation,
            final AssetCategory assetCategory, final RequestInfo requestInfo) {
        final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<VoucherAccountCodeDetails>();
        final String tenantId = revaluation.getTenantId();

        final BigDecimal amount = revaluation.getRevaluationAmount();
        if (assetCategory != null && revaluation.getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                    amount, false, true));
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId,
                    assetCategory.getRevaluationReserveAccount(), amount, true, false));
        } else if (assetCategory != null && revaluation.getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId,
                    revaluation.getFixedAssetsWrittenOffAccount(), amount, false, true));
            accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                    amount, true, false));

        }
        return accountCodeDetails;
    }

}
