package org.egov.wcms.service;

import java.util.List;

import org.egov.wcms.model.SourceType;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.SourceTypeRepository;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SourceTypeService {
    public static final Logger logger = LoggerFactory.getLogger(SourceTypeService.class);

    @Autowired
    private SourceTypeRepository waterSourceTypeRepository;

    @Autowired
    private WaterMasterProducer waterMasterProducer;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    public SourceTypeRequest create(final SourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.persistCreateWaterSourceType(waterSourceRequest);
    }

    public SourceTypeRequest update(final SourceTypeRequest waterSourceRequest) {
        return waterSourceTypeRepository.persistModifyWaterSourceType(waterSourceRequest);
    }

    public SourceType createWaterSource(final String topic, final String key,
            final SourceTypeRequest waterSourceRequest) {
        waterSourceRequest.getWaterSourceType().setCode(codeGeneratorService.generate(SourceType.SEQ_WATERSOURCE));
        final ObjectMapper mapper = new ObjectMapper();
        String waterSourceValue = null;
        try {
            logger.info("createWaterSource service::" + waterSourceRequest);
            waterSourceValue = mapper.writeValueAsString(waterSourceRequest);
            logger.info("waterSourceValue::" + waterSourceValue);
        } catch (final JsonProcessingException e) {
        	logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, waterSourceValue);
        } catch (final Exception ex) {
        	logger.error("Exception Encountered : " + ex);
        }
        return waterSourceRequest.getWaterSourceType();
    }

    public SourceType updateWaterSource(final String topic, final String key,
            final SourceTypeRequest waterSourceRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String waterSourceValue = null;
        try {
            logger.info("updateWaterSource service::" + waterSourceRequest);
            waterSourceValue = mapper.writeValueAsString(waterSourceRequest);
            logger.info("waterSourceValue::" + waterSourceValue);
        } catch (final JsonProcessingException e) {
        	logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, waterSourceValue);
        } catch (final Exception ex) {
        	logger.error("Exception Encountered : " + ex);
        }
        return waterSourceRequest.getWaterSourceType();
    }

    public boolean getWaterSourceByNameAndCode(final String code, final String name, final String tenantId) {
        return waterSourceTypeRepository.checkWaterSourceTypeByNameAndCode(code, name, tenantId);
    }

    public List<SourceType> getWaterSourceTypes(final SourceTypeGetRequest waterSourceTypeGetRequest) {
        return waterSourceTypeRepository.findForCriteria(waterSourceTypeGetRequest);

    }

}
