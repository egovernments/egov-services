package org.egov.pgrrest.master.service;

import org.egov.pgrrest.master.model.ReceivingModeType;
import org.egov.pgrrest.master.producers.PGRProducer;
import org.egov.pgrrest.master.repository.ReceivingModeTypeRepository;
import org.egov.pgrrest.master.web.contract.ReceivingModeTypeReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReceivingModeTypeService {
	
	public static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeService.class);
	
	  @Autowired
	    private ReceivingModeTypeRepository receivingModeRepository;
	  
	  @Autowired
		private PGRProducer pgrProducer;

	  
	  public ReceivingModeTypeReq create(final ReceivingModeTypeReq modeTypeReq) {
			return receivingModeRepository.persistReceivingModeType(modeTypeReq);
		}

		public ReceivingModeTypeReq update(final ReceivingModeTypeReq modeTypeReq) {
			return receivingModeRepository.persistModifyReceivingModeType(modeTypeReq);
		}

	  
		public ReceivingModeType sendMessage(final String topic, final String key,final ReceivingModeTypeReq modeTypeRequest) {
	    	
	        final ObjectMapper mapper = new ObjectMapper();
	        String receivingModeValue = null;
	        try {
	            logger.info("createReceivingCModeType Request::" + modeTypeRequest);
	            receivingModeValue = mapper.writeValueAsString(modeTypeRequest);
	            logger.info("receivingModeValue::" + receivingModeValue);
	        } catch (final JsonProcessingException e) {
	            logger.error("Exception Encountered : " + e);
	        }
	        try {
	        	pgrProducer.sendMessage(topic, key, receivingModeValue);
	        } catch (final Exception ex) {
	            logger.error("Exception Encountered : " + ex);
	        }
	        return modeTypeRequest.getModeType();
	    }
	  
}
