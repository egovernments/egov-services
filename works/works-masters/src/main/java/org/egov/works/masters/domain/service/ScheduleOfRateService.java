package org.egov.works.masters.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.ScheduleOfRateRepository;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<ScheduleOfRate> create(ScheduleOfRateRequest scheduleOfRateRequest) {
        CommonUtils commonUtils = new CommonUtils();
        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            scheduleOfRate.setId(commonUtils.getUUID());
            scheduleOfRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
            for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                sorRate.setId(commonUtils.getUUID());
                sorRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
            }
            if(scheduleOfRate.getMarketRates()!=null && !scheduleOfRate.getMarketRates().isEmpty()) {
                for (final MarketRate marketRate : scheduleOfRate.getMarketRates()) {
                    marketRate.setId(commonUtils.getUUID());
                    marketRate.setAuditDetails(masterUtils.getAuditDetails(scheduleOfRateRequest.getRequestInfo(), false));
                }
            }
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterSorrateCreateValidatedTopic(), scheduleOfRateRequest);
        return scheduleOfRateRequest.getScheduleOfRates();
    }

    public List<ScheduleOfRate> update(ScheduleOfRateRequest scheduleOfRateRequest) {
        kafkaTemplate.send(propertiesManager.getWorksMasterSorrateUpdateValidatedTopic(), scheduleOfRateRequest);
        return scheduleOfRateRequest.getScheduleOfRates();
    }

    public List<ScheduleOfRate> search(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria) {
        return scheduleOfRateRepository.getScheduleOfRateByCriteria(scheduleOfRateSearchCriteria);
    }

    public ScheduleOfRate getbyId(String id, String tenantId) {
        return scheduleOfRateRepository.getbyId(id, tenantId);
    }

}
