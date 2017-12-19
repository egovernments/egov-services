package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.exception.CustomBindException;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.TransferInwardRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransferinwardsService extends DomainService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${inv.transfer.inward.save.topic}")
	private String createTopic;

	@Value("${inv.transfer.inward.update.key}")
	private String updateTopic;
	
	public List<MaterialReceipt> create(TransferInwardRequest inwaardRequest,String tenantid)
	{
		try{
			kafkaTemplate.send(createTopic, inwaardRequest);
			return inwaardRequest.getTransferInwards();
			
		}catch(CustomBindException e){
			throw e;
		}
	}
	
	public List<MaterialReceipt> update(TransferInwardRequest inwardsRequest, String tenantid)
	{
		try{
			kafkaTemplate.send(updateTopic, inwardsRequest);
			return inwardsRequest.getTransferInwards();
		}catch(CustomBindException e){
			throw e;
		}
	}
	
	public List<MaterialReceipt> search(MaterialReceiptSearch transferinwardsSearch, String tenantid)
	{
		return null;
		
	}

}
