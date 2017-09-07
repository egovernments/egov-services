
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {
    private static final String WCMS_SEARCH_PAGESIZE_DEFAULT = "egov.services.wcms.search.pagesize.default";
    public static final String WCMS_SEARCH_PAGENO_MAX = "egov.services.wcms.search.pageno.max";
    public static final String WCMS_SEARCH_PAGESIZE_MAX = "egov.services.wcms.search.pagesize.max";

    @Value("${kafka.topics.usagetype.create.name}")
    private String createUsageTypeTopicName;

    @Value("${kafka.topics.usagetype.update.name}")
    private String updateUsageTypeTopicName;

    @Value("${kafka.topics.supplytype.create.name}")
    private String createSupplyTypeTopicName;

    @Value("${kafka.topics.supplytype.update.name}")
    private String updateSupplyTypeTopicName;

    @Value("${kafka.topics.servicecharge.create.name}")
    private String createServiceChargeTopicName;

    @Value("${kafka.topics.servicecharge.update.name}")
    private String updateServiceChargeTopicName;

    @Value("${kafka.topics.pipesize.create.name}")
    private String createPipeSizeTopicName;

    @Value("${kafka.topics.pipesize.update.name}")
    private String updatePipeSizeTopicName;

    @Value("${kafka.topics.meterstatus.create.name}")
    private String createMeterStatusTopicName;

    @Value("${kafka.topics.meterstatus.update.name}")
    private String updateMeterStatusTopicName;

    @Value("${kafka.topics.metercost.create.name}")
    private String createMeterCostTopicName;

    @Value("${kafka.topics.metercost.update.name}")
    private String updateMeterCostTopicName;

    @Value("${kafka.topics.documenttype.applicationtype.create.name}")
    private String createDocTypeAppTypeTopicName;

    @Value("${kafka.topics.documenttype.applicationtype.update.name}")
    private String updateDocTypeAppTypeTopicName;

    @Value("${kafka.topics.donation.create.name}")
    private String createDonationTopicName;

    @Value("${kafka.topics.documenttype.create.name}")
    private String createDocumentTypeTopicName;

    @Value("${kafka.topics.documenttype.update.name}")
    private String updateDocumentTypeTopicName;

    @Value("${kafka.topics.donation.update.name}")
    private String updateDonationTopicName;

    @Value("${kafka.topics.sourcetype.create.name}")
    private String createSourceTypeTopicName;

    @Value("${kafka.topics.sourcetype.update.name}")
    private String updateSourceTypeTopicName;

    @Value("${kafka.topics.storagereservoir.create.name}")
    private String createStorageReservoirTopicName;

    @Value("${kafka.topics.storagereservoir.update.name}")
    private String updateStorageReservoirTopicName;

    @Value("${kafka.topics.treatmentplant.create.name}")
    private String createTreatmentPlantTopicName;

    @Value("${kafka.topics.treatmentplant.update.name}")
    private String updateTreatmentPlantTopicName;

    @Value("${kafka.topics.meterwaterrates.create.name}")
    private String createMeterWaterRatesTopicName;

    @Value("${kafka.topics.meterwaterrates.update.name}")
    private String updateMeterWaterRatesTopicName;

    @Value("${kafka.topics.nonmeterwaterrates.create.name}")
    private String createNonMeterWaterRatesTopicName;

    @Value("${kafka.topics.nonmeterwaterrates.update.name}")
    private String updateNonMeterWaterRatesTopicName;

    @Value("${kafka.topics.gapcode.create.name}")
    private String createGapcodeTopicName;

    @Value("${kafka.topics.gapcode.update.name}")
    private String updateGapcodeTopicName;

    @Autowired
    private Environment environment;

    public String wcmsSearchPageSizeDefault() {
        return environment.getProperty(WCMS_SEARCH_PAGESIZE_DEFAULT);
    }

    public String wcmsSearchPageNumberMax() {
        return environment.getProperty(WCMS_SEARCH_PAGENO_MAX);
    }

    public String wcmsSearchPageSizeMax() {
        return environment.getProperty(WCMS_SEARCH_PAGESIZE_MAX);
    }

    public String getCreateUsageTypeTopicName() {
        return createUsageTypeTopicName;
    }

    public String getUpdateUsageTypeTopicName() {
        return updateUsageTypeTopicName;
    }

    public String getCreatePipeSizetopicName() {
        return createPipeSizeTopicName;
    }

    public String getUpdatePipeSizeTopicName() {
        return updatePipeSizeTopicName;
    }

    public String getCreateMeterStatusTopicName() {
        return createMeterStatusTopicName;
    }

    public String getUpdateMeterStatusTopicName() {
        return updateMeterStatusTopicName;
    }

    public String getUpdateDocumentTypeTopicName() {
        return updateDocumentTypeTopicName;
    }

    public String getUpdateDocumentTypeApplicationTypeTopicName() {
        return updateDocTypeAppTypeTopicName;
    }

    public String getCreateMeterCostTopicName() {
        return createMeterCostTopicName;
    }

    public String getCreateDocumentTypeApplicationTypeTopicName() {
        return createDocTypeAppTypeTopicName;
    }

    public String getCreateDonationTopicName() {
        return createDonationTopicName;
    }

    public String getUpdateDonationTopicName() {
        return updateDonationTopicName;
    }

    public String getCreateSourceTypeTopicName() {
        return createSourceTypeTopicName;
    }

    public String getUpdateSourceTypeTopicName() {
        return updateSourceTypeTopicName;
    }

    public String getCreateDocumentTypeTopicName() {
        return createDocumentTypeTopicName;
    }

    public String getCreateSupplyTypeTopicName() {
        return createSupplyTypeTopicName;
    }

    public String getUpdateSupplyTypeTopicName() {
        return updateSupplyTypeTopicName;
    }

    public String getCreateStorageReservoirTopicName() {
        return createStorageReservoirTopicName;
    }

    public String getUpdateStorageReservoirTopicName() {
        return updateStorageReservoirTopicName;
    }

    public String getCreateTreatmentPlantTopicName() {
        return createTreatmentPlantTopicName;
    }

    public String getUpdateTreatmentPlantTopicName() {
        return updateTreatmentPlantTopicName;
    }

    public String getCreateMeterWaterRatesTopicName() {
        return createMeterWaterRatesTopicName;
    }

    public String getUpdateMeterWaterRatesTopicName() {
        return updateMeterWaterRatesTopicName;
    }

    public String getUpdateMeterCostTopicName() {
        return updateMeterCostTopicName;
    }

    public String getCreateNonMeterWaterRatesTopicName() {
        return createNonMeterWaterRatesTopicName;
    }

    public String getUpdateNonMeterWaterRatesTopicName() {
        return updateNonMeterWaterRatesTopicName;
    }

    public String getCreateServiceChargeTopicName() {
        return createServiceChargeTopicName;
    }

    public String getUpdateServiceChargeTopicName() {
        return updateServiceChargeTopicName;
    }

    public String getCreateGapcodeTopicName() {
        return createGapcodeTopicName;
    }

    public String getUpdateGapcodeTopicName() {
        return updateGapcodeTopicName;
    }
}