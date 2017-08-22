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

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.model.MeterWaterRates;
import org.egov.wcms.model.enums.BillingType;
import org.egov.wcms.repository.MeterWaterRatesRepository;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.egov.wcms.web.contract.UsageTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MeterWaterRatesService {

    @Autowired
    private MeterWaterRatesRepository meterWaterRatesRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private RestWaterExternalMasterService restExternalMasterService;

    public MeterWaterRatesRequest create(final MeterWaterRatesRequest meterWaterRatesRequest) {
        return meterWaterRatesRepository.persistCreateMeterWaterRates(meterWaterRatesRequest);
    }

    public MeterWaterRatesRequest update(final MeterWaterRatesRequest meterWaterRatesRequest) {
        return meterWaterRatesRepository.persistUpdateMeterWaterRates(meterWaterRatesRequest);
    }

    public List<MeterWaterRates> createMeterWaterRates(final String topic, final String key,
            final MeterWaterRatesRequest meterWaterRatesRequest) {
        for (final MeterWaterRates meterWaterRates : meterWaterRatesRequest.getMeterWaterRates()) {
            meterWaterRates.setBillingType(BillingType.METERED.toString());
            meterWaterRates.setCode(codeGeneratorService.generate(MeterWaterRates.SEQ_METERWATERRATES));
        }

        try {
            kafkaTemplate.send(topic, key, meterWaterRatesRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return meterWaterRatesRequest.getMeterWaterRates();
    }

    public List<MeterWaterRates> updateMeterWaterRates(final String topic, final String key,
            final MeterWaterRatesRequest meterWaterRatesRequest) {
        for (final MeterWaterRates meterWaterRates : meterWaterRatesRequest.getMeterWaterRates()) 
            meterWaterRates.setBillingType(BillingType.METERED.toString());
        try {
            kafkaTemplate.send(topic, key, meterWaterRatesRequest);
        } catch (final Exception ex) {
            log.error("Exception Encountered : " + ex);
        }
        return meterWaterRatesRequest.getMeterWaterRates();
    }

    public List<MeterWaterRates> getMeterWaterRates(
            final MeterWaterRatesGetRequest meterWaterRatesGetRequest) {

        if (meterWaterRatesGetRequest.getUsageTypeName() != null) {
            final UsageTypeResponse usageType = restExternalMasterService.getUsageIdFromPTModule(
                    meterWaterRatesGetRequest.getUsageTypeName(),WcmsConstants.WC, meterWaterRatesGetRequest.getTenantId());
            if (usageType.getUsageTypesSize())
                meterWaterRatesGetRequest.setUsageTypeId(usageType.getUsageMasters().get(0).getId());

        }
        return meterWaterRatesRepository.findForCriteria(meterWaterRatesGetRequest);
    }

    public boolean checkMeterWaterRatesExists(final MeterWaterRates meterWaterRates) {
        getUsageTypeByName(meterWaterRates);
        return meterWaterRatesRepository.checkMeterWaterRatesExists(meterWaterRates.getCode(),
                meterWaterRates.getUsageTypeId(),
                meterWaterRates.getSourceTypeName(), meterWaterRates.getPipeSize(),
                meterWaterRates.getTenantId());
    }

    public Boolean getUsageTypeByName(final MeterWaterRates meterWaterRates) {
        Boolean isValidUsage = Boolean.FALSE;
        final UsageTypeResponse usageType = restExternalMasterService.getUsageIdFromPTModule(
                meterWaterRates.getUsageTypeName(),WcmsConstants.WC,
                meterWaterRates.getTenantId());
        if (usageType.getUsageTypesSize()) {
            isValidUsage = Boolean.TRUE;
            meterWaterRates
                    .setUsageTypeId(usageType.getUsageMasters() != null && usageType.getUsageMasters().get(0) != null
                            ? usageType.getUsageMasters().get(0).getId() : "");

        }

        return isValidUsage;

    }

    public boolean checkPipeSizeExists(final Double pipeSize, final String tenantId) {
        return meterWaterRatesRepository.checkPipeSizeExists(pipeSize, tenantId);
    }

    public boolean checkSourceTypeExists(final String sourceTypeName, final String tenantId) {
        return meterWaterRatesRepository.checkSourceTypeExists(sourceTypeName, tenantId);
    }

}
