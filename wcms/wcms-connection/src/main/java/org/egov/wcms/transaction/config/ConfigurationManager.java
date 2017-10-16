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
package org.egov.wcms.transaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ConfigurationManager {

    @Value("${egov.services.bill_service.searchpropertytaxdue}")
    private String billingServiceSearchDuesTopic;

    @Value("${kafka.topics.save.wcms}")
    private String kafkaSaveWaterConnectionTopic;

    @Value("${kafka.topics.update.wcms}")
    private String kafkaUpdateWaterConnectionTopic;
    
    @Value("${egov.services.wcms_masters_sourcetype.searchpath}")
    private String waterMasterServiceSourceSearchPathTopic;

    @Value("${egov.services.wcms_masters_supplytype.searchpath}")
    private String waterMasterServiceSupplySearchPathTopic;

    @Value("${egov.services.wcms_masters_pipesize.searchpath}")
    private String waterMasterServicePipesizeSearchPathTopic;

    @Value("${egov.services.wcms_masters_donation.searchpath}")
    private String waterMasterServiceDonationSearchPathTopic;

    @Value("${egov.services.wcms_masters.hostname}")
    private String waterMasterServiceBasePathTopic;

    @Value("${egov.services.id_service.hostname}")
    private String idGenServiceBasePathTopic;

    @Value("${egov.services.id_service.createpath}")
    private String idGenServiceCreatePathTopic;
 
    @Value("${id.idName}")
    private String idGenNameServiceTopic;
    
    @Value("${id.format}")
    private String idGenFormatServiceTopic;
    
    @Value("${id.usernameFormat}")
    private String userNameFormat;
    
    @Value("${id.userName}")
    private String userNameService;
    
    @Value("${businessService}")
    private String businessService;
    
    @Value("${id.hscName}")
    private String hscGenNameServiceTopic;
    
    @Value("${id.hscFormat}")
    private String hscGenFormatServiceTopic;
    
    @Value("${id.EstName}")
    private String estimateGenNameServiceTopic;
    
    @Value("${id.EstFormat}")
    private String estimateGenFormatServiceTopic;
    
    @Value("${id.WOName}")
    private String workOrderGenNameServiceTopic; 
    
    @Value("${id.WOFormat}")
    private String workOrderGenFormatServiceTopic;
    
    @Value("${id.demandBillName}")
    private String demandBillGenNameServiceTopic;
    
    @Value("${id.demandBillFormat}")
    private String demandBillGenFormatServiceTopic;
    
    @Value("${egov.services.demandbill_servivc.searchdemanddetail}")
    private String searchDemandDEtailExist;
    
    @Value("${egov.services.finance_service.hostname}")
    private String financeServiceHostName; 
    
    @Value("${egov.services.finance_service.searchpath}")
    private String financeServiceSearchPath; 
    
    @Value("${egov.services.demandbill_service.hostname}")
    private String billingDemandServiceHostNameTopic;
    
    @Value("${egov.services.demandbill_servivc.updatedemanddet}")
    private String billingUpdateDemand;
    
    @Value("${egov.services.demandbill_service.createdemand}")
    private String createbillingDemandServiceTopic;
    
    @Value("${egov.services.demandbill_servivc.updatedemand}")
    private String updateDemandServiceTopic;
    
    @Value("${egov.services.demandbill_servivc.searchdemand}")
    private String searchbillingDemandServiceTopic;
    
    @Value("${egov.services.demandbill_service.taxperiod}")
    private String taxperidforfinancialYearTopic;
    
    @Value("${egov.services.wcms_masters.treatment}")
    private String waterTreatmentSearchTopic;
    
    @Value("${egov.services.wcms.masters.reservoir}") 
    private String reservoirSearchTopic;
    
    @Value("${egov.services.pt_property.hostname}")
    private String propertyServiceHostNameTopic;
    
    @Value("${egov.services.pt_property_propertytype.searchpath}")
    private String propertyServiceSearchPathTopic;
    
    @Value("${egov.services.demandbill_service.taxheadperiod}")
    private String taxHeadMasterNameTopic;
    
    @Value("${estimation.notice.placeholders}")
    private String estimationNoticePlaceHolders;
    
    @Value("${egov.services.boundary_service.hostname}")
    private String locationServiceBasePathTopic;

    @Value("${egov.services.boundary_service.searchpath}")
    private String locationServiceBoundarySearchPathTopic;
    
    @Value("${egov.services.egov_user.hostname}")
    private String userHostName;

    @Value("${egov.services.egov_user.basepath}")
    private String userBasePath;
    
    @Value("${egov.services.egov_user.searchpath}")
    private String userSearchPath;
    
    @Value("${egov.services.egov_user.createpath}")
    private String userCreatePath;
    
    @Value("${egov.services.egov_user.updatepath}")
    private String userUpdatePath;
    
    @Value("${default.password}")
    private String defaultPassword;
    
    @Value("${egov.services.tenant.host}")
    private String tenantServiceBasePath;
    
    @Value("${egov.services.tenant.searchpath}")
    private String tenantServiceSearchPath;
    
    @Value("${id.hscNumberOfChar}")
    private Integer hscNumberOfChar; 
    
    @Value("${egov.services.wcms_masters.waterChargesConfig.searchpath}")
    private String waterMasterServiceWaterChargesConfigSearchPathTopic;
    
    @Value("${demand.periodcycle.annual.keyname}")
    private String periodCycleAnnualKeyName; 
    
    @Value("${demand.periodcycle.halfyear.keyname}")
    private String periodCycleHalfYearKeyName; 
    
    @Value("${demand.periodcycle.quarter.keyname}")
    private String periodCycleQuarterlyKeyName; 
    
    @Value("${demand.periodcycle.month.keyname}")
    private String periodCycleMonthltKeyName; 
    
    @Value("${demand.periodcycle.default.keyname}") 
    private String periodCycleDefaultKeyName; 
    
    @Value("${egov.services.wcms_masters_usageType.searchpath}")
    private String usageTypeSearchPathTopic;
    
    @Value("${egov.services.wcms_masters.nonMeterWaterRates.searchpath}")
    private String nonMeterWaterRatesSearchPath; 
    
}