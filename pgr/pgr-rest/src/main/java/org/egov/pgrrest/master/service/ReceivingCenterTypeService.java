package org.egov.pgrrest.master.service;

import org.egov.pgrrest.master.model.ReceivingCenterType;
import org.egov.pgrrest.master.producers.PGRProducer;
import org.egov.pgrrest.master.repository.ReceivingCenterTypeRepository;
import org.egov.pgrrest.master.web.contract.ReceivingCenterTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReceivingCenterTypeService {
	
	 public static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeService.class);

	@Autowired
	private ReceivingCenterTypeRepository receivingCenterRepository;
	
	@Autowired
	private PGRProducer pgrProducer;

	public ReceivingCenterTypeReq create(final ReceivingCenterTypeReq centerTypeReq) {
		return receivingCenterRepository.persistReceivingCenterType(centerTypeReq);
	}

	public ReceivingCenterTypeReq update(final ReceivingCenterTypeReq centerTypeReq) {
		return receivingCenterRepository.persistModifyReceivingCenterType(centerTypeReq);
	}
	
	
    public ReceivingCenterType sendMessage(final ReceivingCenterTypeReq centerTypeRequest) {
    	
        final ObjectMapper mapper = new ObjectMapper();
        String receivingCenterValue = null;
        
        String topic = "egov.pgr.receiving-centertype-create";
        String key = "receiving-centertype-create";
        try {
            logger.info("createReceivingCenterType Request::" + centerTypeRequest);
            receivingCenterValue = mapper.writeValueAsString(centerTypeRequest);
            logger.info("categoryValue::" + receivingCenterValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
        	pgrProducer.sendMessage(topic, key, receivingCenterValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return centerTypeRequest.getCenterType();
    }

}
