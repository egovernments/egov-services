package org.egov.wcms.service;

import java.util.List;

import org.egov.wcms.model.WaterSourceType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.WaterSourceTypeRepository;
import org.egov.wcms.web.contract.WaterSourceTypeGetRequest;
import org.egov.wcms.web.contract.WaterSourceTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WaterSourceTypeService {
    public static final Logger logger = LoggerFactory.getLogger(WaterSourceTypeService.class);

    @Autowired
    private WaterSourceTypeRepository waterSourceTypeRepository;

    @Autowired
    private WaterMasterProducer waterMasterProducer;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public WaterSourceTypeRequest create(final WaterSourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.persistCreateWaterSourceType(waterSourceRequest);
    }

    public WaterSourceTypeRequest update(final WaterSourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.persistModifyWaterSourceType(waterSourceRequest);
    }

    public WaterSourceType createWaterSource(final String topic, final String key,
            final WaterSourceTypeRequest waterSourceRequest) {
        waterSourceRequest.getWaterSourceType().setCode(codeGeneratorService.generate(WaterSourceType.SEQ_WATERSOURCE));
        final ObjectMapper mapper = new ObjectMapper();
        String waterSourceValue = null;
        try {
            logger.info("createWaterSource service::" + waterSourceRequest);
            waterSourceValue = mapper.writeValueAsString(waterSourceRequest);
            logger.info("waterSourceValue::" + waterSourceValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            waterMasterProducer.sendMessage(topic, key, waterSourceValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return waterSourceRequest.getWaterSourceType();
    }

    public WaterSourceType updateWaterSource(final String topic, final String key,
            final WaterSourceTypeRequest waterSourceRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String waterSourceValue = null;
        try {
            logger.info("updateWaterSource service::" + waterSourceRequest);
            waterSourceValue = mapper.writeValueAsString(waterSourceRequest);
            logger.info("waterSourceValue::" + waterSourceValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            waterMasterProducer.sendMessage(topic, key, waterSourceValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return waterSourceRequest.getWaterSourceType();
    }

    public boolean getWaterSourceByNameAndCode(final String code, final String name, final String tenantId) {
        return waterSourceTypeRepository.checkWaterSourceTypeByNameAndCode(code, name, tenantId);
    }

    public List<WaterSourceType> getWaterSourceTypes(final WaterSourceTypeGetRequest waterSourceTypeGetRequest) {
        return waterSourceTypeRepository.findForCriteria(waterSourceTypeGetRequest);

    }

}
