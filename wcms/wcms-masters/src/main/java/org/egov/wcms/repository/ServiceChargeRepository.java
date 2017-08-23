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
    public ServiceChargeReq persistUpdateServiceChargeRequestToDB(final ServiceChargeReq serviceChargeRequest){
        final List<ServiceCharge> listOfServiceCharges = serviceChargeRequest.getServiceCharge();
        final List<ServiceChargeDetails> listOfServiceChargeDetail = new ArrayList<>();
        for (final ServiceCharge servicecharge : listOfServiceCharges)
            listOfServiceChargeDetail.addAll(servicecharge.getChargeDetails());
        final List<Map<String, Object>> batchValues = new ArrayList<>(listOfServiceCharges.size());
        final List<Map<String, Object>> batchArgs = new ArrayList<>(listOfServiceCharges.size());
        final List<Map<String, Object>> batchArg = new ArrayList<>(listOfServiceChargeDetail.size());
        String updateServiceCharge = serviceChargeQueryBuilder.updateServiceChargeData();
        String deleteServiceChargeDetails = serviceChargeQueryBuilder.deleteServiceChargeDetailsData();
        String insertServiceChargeDetails = serviceChargeQueryBuilder.insertServiceChargeDetailsData();
        for (final ServiceCharge serviceCharge : listOfServiceCharges) {
            batchValues.add(new MapSqlParameterSource("servicetype",serviceCharge.getServiceType() )
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
            batchArgs.add(new MapSqlParameterSource("servicecharge",Long.valueOf(serviceCharge.getCode())).
                    addValue(TENANT, serviceCharge.getTenantId()).getValues());
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
        namedParameterJdbcTemplate.batchUpdate(deleteServiceChargeDetails, batchArgs.toArray(new Map[listOfServiceCharges.size()]));
        namedParameterJdbcTemplate.batchUpdate(insertServiceChargeDetails, batchArg.toArray(new Map[listOfServiceChargeDetail.size()]));
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

    public List<ServiceCharge> pushServiceChargeUpdateReqToQueue(ServiceChargeReq serviceChargeRequest) {
        logger.info("Pushing ServiceChargeUpdateRequest to queue");
        try {
            kafkaTemplate.send(applicationProperties.getUpdateServiceChargeTopicName(), serviceChargeRequest);
        } catch (final Exception e) {
            logger.error("Exception Encountered :" + e);
        }
        return serviceChargeRequest.getServiceCharge();
    }

}
