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

import java.util.List;
import java.util.Set;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.CurrentValueRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentValueService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private CurrentValueRepository currentValueRepository;

    @Autowired
    private SequenceGenService sequenceGenService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public AssetCurrentValueResponse getCurrentValues(final Set<Long> assetIds, final String tenantId,
            final RequestInfo requestInfo) {

        return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                currentValueRepository.getCurrentValues(assetIds, tenantId));
    }

    public AssetCurrentValueResponse createCurrentValueAsync(final AssetCurrentValueRequest assetCurrentValueRequest) {

        final RequestInfo requestInfo = assetCurrentValueRequest.getRequestInfo();
        final List<AssetCurrentValue> assetCurrentValues = assetCurrentValueRequest.getAssetCurrentValues();
        final AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo);

        final List<Long> idList = sequenceGenService.getIds(assetCurrentValues.size(),
                Sequence.CURRENTVALUESEQUENCE.toString());
        int i = 0;
        for (final AssetCurrentValue assetCurrentValue : assetCurrentValues) {
            assetCurrentValue.setAuditDetails(auditDetails);
            assetCurrentValue.setId(idList.get(i++));
        }
        kafkaTemplate.send(applicationProperties.getSaveCurrentvalueTopic(), assetCurrentValueRequest);
        return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                assetCurrentValues);
    }

    public void saveCurrentValue(final AssetCurrentValueRequest assetCurrentValueRequest) {
        currentValueRepository.create(assetCurrentValueRequest.getAssetCurrentValues());
    }

    public void saveUpdatedCurrentValue(final AssetCurrentValueRequest assetCurrentValueRequest) {
        currentValueRepository.update(assetCurrentValueRequest.getAssetCurrentValues());
    }

}
