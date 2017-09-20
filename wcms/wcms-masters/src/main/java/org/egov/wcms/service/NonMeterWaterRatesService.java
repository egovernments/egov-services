/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.wcms.service;

import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.NonMeterWaterRates;
import org.egov.wcms.model.enums.BillingType;
import org.egov.wcms.repository.NonMeterWaterRatesRepository;
import org.egov.wcms.web.contract.NonMeterWaterRatesGetReq;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NonMeterWaterRatesService {

    @Autowired
    private NonMeterWaterRatesRepository nonMeterWaterRatesRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public NonMeterWaterRatesReq create(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        return nonMeterWaterRatesRepository.create(nonMeterWaterRatesReq);
    }

    public NonMeterWaterRatesReq update(final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        return nonMeterWaterRatesRepository.update(nonMeterWaterRatesReq);
    }

    public List<NonMeterWaterRates> pushCreateToQueue(final String topic, final String key,
            final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesReq.getNonMeterWaterRates()) {
            nonMeterWaterRates.setBillingType(BillingType.NONMETERED.toString());
            nonMeterWaterRates.setCode(codeGeneratorService.generate(NonMeterWaterRates.SEQ_NONMETERWATERRATES));
        }

        try {
            kafkaTemplate.send(topic, key, nonMeterWaterRatesReq);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return nonMeterWaterRatesReq.getNonMeterWaterRates();
    }

    public List<NonMeterWaterRates> pushUpdateToQueue(final String topic, final String key,
            final NonMeterWaterRatesReq nonMeterWaterRatesReq) {
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesReq.getNonMeterWaterRates())
            nonMeterWaterRates.setBillingType(BillingType.NONMETERED.toString());
        try {
            kafkaTemplate.send(topic, key, nonMeterWaterRatesReq);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return nonMeterWaterRatesReq.getNonMeterWaterRates();
    }

    public List<NonMeterWaterRates> getNonMeterWaterRates(
            final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest) {
        return nonMeterWaterRatesRepository.findForCriteria(nonMeterWaterRatesGetRequest);
    }

    public boolean checkNonMeterWaterRatesExists(final NonMeterWaterRates nonMeterWaterRates) {
        isUsageTypeExists(nonMeterWaterRates);
        return nonMeterWaterRatesRepository.checkNonMeterWaterRatesExists(nonMeterWaterRates.getCode(),
                nonMeterWaterRates.getConnectionType(), nonMeterWaterRates.getUsageTypeId(),
                nonMeterWaterRates.getSubUsageTypeId(),
                nonMeterWaterRates.getSourceTypeName(), nonMeterWaterRates.getPipeSize(), nonMeterWaterRates.getFromDate(),
                nonMeterWaterRates.getTenantId());
    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        return nonMeterWaterRatesRepository.checkPipeSizeExists(pipeSize, tenantId);
    }

    public boolean checkSourceTypeExists(final String sourceTypeName, final String tenantId) {
        return nonMeterWaterRatesRepository.checkSourceTypeExists(sourceTypeName, tenantId);
    }

    public boolean isUsageTypeExists(final NonMeterWaterRates nonMeterWaterRates) {

        final Map<String, Object> usageTypes = nonMeterWaterRatesRepository
                .checkUsageAndSubUsageTypeExists(nonMeterWaterRates.getUsageTypeCode(), nonMeterWaterRates.getTenantId());
        if (usageTypes.isEmpty())
            return false;

        for (String key : usageTypes.keySet()) {
            nonMeterWaterRates.setUsageTypeId((Long) usageTypes.get(key));
        }

        return true;
    }

    public boolean isSubUsageTypeExists(final NonMeterWaterRates nonMeterWaterRates) {

        final Map<String, Object> subUsageTypes = nonMeterWaterRatesRepository
                .checkUsageAndSubUsageTypeExists(nonMeterWaterRates.getSubUsageTypeCode(), nonMeterWaterRates.getTenantId());
        if (subUsageTypes.isEmpty())
            return false;

        for (String key : subUsageTypes.keySet()) {
            nonMeterWaterRates.setSubUsageTypeId((Long) subUsageTypes.get(key));
        }
        return true;
    }

}
