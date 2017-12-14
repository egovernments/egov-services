package org.egov.works.masters.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.ScheduleOfRateRepository;
import org.egov.works.masters.domain.validator.ScheduleOfRateValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ramki on 31/10/17.
 */

@Service
public class ScheduleOfRateService {

    @Autowired
    private ScheduleOfRateRepository scheduleOfRateRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MasterUtils masterUtils;

    @Autowired
    private ScheduleOfRateValidator scheduleOfRateValidator;

    @Transactional
    public ResponseEntity<?> create(ScheduleOfRateRequest scheduleOfRateRequest) {
        ScheduleOfRateResponse response = new ScheduleOfRateResponse();
        CommonUtils commonUtils = new CommonUtils();

        scheduleOfRateValidator.validate(scheduleOfRateRequest);
        scheduleOfRateValidator.validateForExistance(scheduleOfRateRequest);

        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            scheduleOfRate.setId(commonUtils.getUUID());
            scheduleOfRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
            for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                sorRate.setId(commonUtils.getUUID());
                sorRate.setScheduleOfRate(scheduleOfRate.getId());
                sorRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
            }
            if (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty()) {
                for (final MarketRate marketRate : scheduleOfRate.getMarketRates()) {
                    marketRate.setId(commonUtils.getUUID());
                    marketRate.setScheduleOfRate(scheduleOfRate.getId());
                    marketRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
                }
            }
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterSorrateSaveOrUpdateValidatedTopic(), scheduleOfRateRequest);
        response.setScheduleOfRates(scheduleOfRateRequest.getScheduleOfRates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(scheduleOfRateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update(ScheduleOfRateRequest scheduleOfRateRequest) {
        ScheduleOfRateResponse response = new ScheduleOfRateResponse();

        scheduleOfRateValidator.validate(scheduleOfRateRequest);
        scheduleOfRateValidator.validateForUpdate(scheduleOfRateRequest);

        SORRate sorRateP = null;
        SORRate sorRateC = null;

        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            scheduleOfRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), true));
            Collections.sort(scheduleOfRate.getSorRates(), new Comparator<SORRate>() {
                @Override
                public int compare(SORRate sorRate1, SORRate sorRate2) {
                    return sorRate1.getFromDate().compareTo(sorRate2.getFromDate());
                }
            });
            for (int i = 0; i < scheduleOfRate.getSorRates().size(); i++) {
                SORRate sorRate = scheduleOfRate.getSorRates().get(i);
                if (i == 0) {
                    sorRateP = sorRate;
                    sorRateC = sorRate;
                } else {
                    sorRateP = sorRateC;
                    sorRateC = sorRate;
                }
                if (sorRateP.getToDate() > sorRateC.getFromDate()) {
                    scheduleOfRateValidator.validateRatesForUpdate(scheduleOfRateRequest, scheduleOfRate.getId(), sorRateP.getFromDate(), sorRateP.getToDate(), sorRateP.getTenantId());
                }

                if (sorRate.getToDate() == null) {

                }
                sorRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), true));
            }
            if (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty()) {
                for (final MarketRate marketRate : scheduleOfRate.getMarketRates()) {
                    marketRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), true));
                }
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMasterSorrateSaveOrUpdateValidatedTopic(), scheduleOfRateRequest);

        response.setScheduleOfRates(scheduleOfRateRequest.getScheduleOfRates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(scheduleOfRateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<ScheduleOfRate> search(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria) {
        return scheduleOfRateRepository.getScheduleOfRateByCriteria(scheduleOfRateSearchCriteria);
    }

    public ScheduleOfRate getById(String id, String tenantId) {
        return scheduleOfRateRepository.getbyId(id, tenantId);
    }

    public ScheduleOfRate getByCodeCategory(String code, String scheduleCategory, String tenantId, String sorId, Boolean IsUpdateUniqueCheck) {
        return scheduleOfRateRepository.getByCodeCategory(code, scheduleCategory, tenantId, sorId, IsUpdateUniqueCheck);
    }
}
