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
package org.egov.wcms.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.model.ServiceChargeDetails;
import org.egov.wcms.repository.builder.ServiceChargeQueryBuilder;
import org.egov.wcms.service.CodeGeneratorService;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceChargeRepository {

    public static final String TENANT = "tenantid";
    public static final String SERVICECHARGE = "servicecharge";

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ServiceChargeQueryBuilder serviceChargeQueryBuilder;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(ServiceChargeRepository.class);

    @SuppressWarnings("unchecked")
    public ServiceChargeReq persistCreateServiceChargeToDb(final ServiceChargeReq serviceChargeRequest) {
        final List<ServiceCharge> listOfServiceCharges = serviceChargeRequest.getServiceCharge();
        final String insertServiceCharge = serviceChargeQueryBuilder.insertServiceChargeData();
        final String insertServiceChargeDetails = serviceChargeQueryBuilder.insertServiceChargeDetailsData();
        final List<ServiceChargeDetails> listOfServiceChargeDetail = new ArrayList<>();
        for (final ServiceCharge servicecharge : listOfServiceCharges)
            listOfServiceChargeDetail.addAll(servicecharge.getChargeDetails());
        final List<Map<String, Object>> batchValues = new ArrayList<>(listOfServiceCharges.size());
        final List<Map<String, Object>> batchArgs = new ArrayList<>(listOfServiceChargeDetail.size());
        for (final ServiceCharge serviceCharge : listOfServiceCharges) {
            final String sequence = codeGeneratorService.generate(ServiceCharge.SEQ_SERVICECHARGE);
            batchValues.add(new MapSqlParameterSource("id", Long.valueOf(sequence)).addValue("code", sequence)
                    .addValue("servicetype", serviceCharge.getServiceType())
                    .addValue("servicechargeapplicable", serviceCharge.getServiceChargeApplicable())
                    .addValue("servicechargetype", serviceCharge.getServiceChargeType())
                    .addValue("description", serviceCharge.getDescription())
                    .addValue("effectivefrom", serviceCharge.getEffectiveFrom())
                    .addValue("effectiveto", serviceCharge.getEffectiveTo()).addValue("outsideulb", serviceCharge.getOutsideUlb())
                    .addValue(TENANT, serviceCharge.getTenantId()).addValue("createdby", serviceChargeRequest.getRequestInfo()
                            .getUserInfo().getId())
                    .addValue("createddate", new Date().getTime())
                    .addValue("lastmodifiedby", serviceChargeRequest.getRequestInfo()
                            .getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime()).getValues());
            final List<ServiceChargeDetails> listOfServiceChargeDetails = serviceCharge.getChargeDetails();
            for (final ServiceChargeDetails serviceChargeDetail : listOfServiceChargeDetails) {
                final String sequenceDetail = codeGeneratorService.generate(ServiceChargeDetails.SEQ_SERVICECHARGEDETAILS);
                batchArgs.add(new MapSqlParameterSource("id", Long.valueOf(sequenceDetail)).addValue("code", sequenceDetail)
                        .addValue("uomfrom", serviceChargeDetail.getUomFrom()).addValue("uomto", serviceChargeDetail.getUomTo())
                        .addValue("amountorpercentage", serviceChargeDetail.getAmountOrpercentage()).addValue(SERVICECHARGE,
                                Long.valueOf(sequence))
                        .addValue(TENANT, serviceChargeDetail.getTenantId())
                        .getValues());
            }
        }
        namedParameterJdbcTemplate.batchUpdate(insertServiceCharge, batchValues.toArray(new Map[listOfServiceCharges.size()]));
        namedParameterJdbcTemplate.batchUpdate(insertServiceChargeDetails,
                batchArgs.toArray(new Map[listOfServiceChargeDetail.size()]));
        return serviceChargeRequest;
    }

    @SuppressWarnings("unchecked")
    public ServiceChargeReq persistUpdateServiceChargeRequestToDB(final ServiceChargeReq serviceChargeRequest) {
        final List<ServiceCharge> listOfServiceCharges = serviceChargeRequest.getServiceCharge();
        final List<ServiceChargeDetails> listOfServiceChargeDetail = new ArrayList<>();
        for (final ServiceCharge servicecharge : listOfServiceCharges)
            listOfServiceChargeDetail.addAll(servicecharge.getChargeDetails());
        final List<Map<String, Object>> batchValues = new ArrayList<>(listOfServiceCharges.size());
        final List<Map<String, Object>> batchArgs = new ArrayList<>(listOfServiceCharges.size());
        final List<Map<String, Object>> batchArg = new ArrayList<>(listOfServiceChargeDetail.size());
        final String updateServiceCharge = serviceChargeQueryBuilder.updateServiceChargeData();
        final String deleteServiceChargeDetails = serviceChargeQueryBuilder.deleteServiceChargeDetailsData();
        final String insertServiceChargeDetails = serviceChargeQueryBuilder.insertServiceChargeDetailsData();
        for (final ServiceCharge serviceCharge : listOfServiceCharges) {
            batchValues.add(new MapSqlParameterSource("servicetype", serviceCharge.getServiceType())
                    .addValue("servicechargeapplicable", serviceCharge.getServiceChargeApplicable())
                    .addValue("servicechargetype", serviceCharge.getServiceChargeType())
                    .addValue("description", serviceCharge.getDescription())
                    .addValue("effectivefrom", serviceCharge.getEffectiveFrom())
                    .addValue("effectiveto", serviceCharge.getEffectiveTo()).addValue("outsideulb", serviceCharge.getOutsideUlb())
                    .addValue("lastmodifiedby", serviceChargeRequest.getRequestInfo()
                            .getUserInfo().getId())
                    .addValue("lastmodifieddate", new Date().getTime())
                    .addValue("code", serviceCharge.getCode())
                    .addValue(TENANT, serviceCharge.getTenantId()).getValues());
            batchArgs.add(new MapSqlParameterSource("servicecharge", Long.valueOf(serviceCharge.getCode()))
                    .addValue(TENANT, serviceCharge.getTenantId()).getValues());
            final List<ServiceChargeDetails> listOfServiceChargeDetails = serviceCharge.getChargeDetails();
            for (final ServiceChargeDetails serviceChargeDetail : listOfServiceChargeDetails) {
                final String sequenceDetail = codeGeneratorService.generate(ServiceChargeDetails.SEQ_SERVICECHARGEDETAILS);
                batchArg.add(new MapSqlParameterSource("id", Long.valueOf(sequenceDetail)).addValue("code", sequenceDetail)
                        .addValue("uomfrom", serviceChargeDetail.getUomFrom()).addValue("uomto", serviceChargeDetail.getUomTo())
                        .addValue("amountorpercentage", serviceChargeDetail.getAmountOrpercentage()).addValue(SERVICECHARGE,
                                Long.valueOf(serviceCharge.getCode()))
                        .addValue(TENANT, serviceChargeDetail.getTenantId())
                        .getValues());
            }
        }
        namedParameterJdbcTemplate.batchUpdate(updateServiceCharge, batchValues.toArray(new Map[listOfServiceCharges.size()]));
        namedParameterJdbcTemplate.batchUpdate(deleteServiceChargeDetails,
                batchArgs.toArray(new Map[listOfServiceCharges.size()]));
        namedParameterJdbcTemplate.batchUpdate(insertServiceChargeDetails,
                batchArg.toArray(new Map[listOfServiceChargeDetail.size()]));
        return serviceChargeRequest;
    }

    public List<ServiceCharge> pushServiceChargeCreateReqToQueue(final ServiceChargeReq serviceChargeRequest) {
        logger.info("Pushing ServiceChargeCreateRequest to queue");
        try {
            kafkaTemplate.send(applicationProperties.getCreateServiceChargeTopicName(), serviceChargeRequest);
        } catch (final Exception e) {
            logger.error("Exception Encountered :" + e);
        }
        return serviceChargeRequest.getServiceCharge();
    }

    public List<ServiceCharge> pushServiceChargeUpdateReqToQueue(final ServiceChargeReq serviceChargeRequest) {
        logger.info("Pushing ServiceChargeUpdateRequest to queue");
        try {
            kafkaTemplate.send(applicationProperties.getUpdateServiceChargeTopicName(), serviceChargeRequest);
        } catch (final Exception e) {
            logger.error("Exception Encountered :" + e);
        }
        return serviceChargeRequest.getServiceCharge();
    }

}
