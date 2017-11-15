package org.egov.inv.domain.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.OpeningBalanceSearchCriteria;
import org.egov.inv.persistence.repository.IdgenRepository;
import org.egov.inv.persistence.repository.OpeningBalanceRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.swagger.model.MaterialReceipt;
import io.swagger.model.MaterialReceipt.ReceiptTypeEnum;
import io.swagger.model.OpeningBalanceRequest;
import io.swagger.model.OpeningBalanceResponse;

@Service
public class OpeningBalanceService {
	
	@Autowired
	private IdgenRepository idgenRepository;
	
	@Autowired
	private InventoryUtilityService inventoryUtilityService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	@Value("${inv.openbalance.save.topic}")
	private String createTopic;

	@Value("${inv.openbalance.update.topic}")
	private String updateTopic;
	
	@Value("${inv.openbal.idgen.name}")
	private String idGenNameForTargetNumPath;
	
	@Autowired
	private  OpeningBalanceRepository openingBalanceRepository;
	
	public  List<MaterialReceipt> create(OpeningBalanceRequest headerRequest, String tenantId) {

		headerRequest.getMaterialReceipt().stream().forEach(materialReceipt -> {
			materialReceipt.setId(openingBalanceRepository.getSequence(materialReceipt));
			materialReceipt.setMrnStatus("CREATED");
			//materialReceipt.setReceiptType();
			materialReceipt.setReceiptType(ReceiptTypeEnum.valueOf("OPENING_BALANCE"));
			materialReceipt.setMrnNumber(appendString(materialReceipt));
			materialReceipt.getReceiptDetails().stream().forEach(detail ->{
				detail.setId(Integer.valueOf(openingBalanceRepository.getSequence(detail)));
				detail.setTenantId(tenantId);
				detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
					addinfo.setId(Integer.valueOf(openingBalanceRepository.getSequence(addinfo)));
					addinfo.setTenantId(tenantId);
				});
			});
		});
		for (MaterialReceipt material : headerRequest.getMaterialReceipt()) {
			material.setAuditDetails(
					inventoryUtilityService.mapAuditDetails(headerRequest.getRequestInfo(), tenantId));
			material.setId(openingBalanceRepository.getSequence(material));
		}
		kafkaTemplate.send(createTopic, headerRequest);
		return headerRequest.getMaterialReceipt();
	}
	
	public List<MaterialReceipt> update(OpeningBalanceRequest openBalReq, String tenantId) {
		openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
			materialReceipt.getReceiptDetails().stream().forEach(detail ->{
				detail.setTenantId(tenantId);
				detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
					addinfo.setTenantId(tenantId);
				});
			});
		});
		
		for (MaterialReceipt material : openBalReq.getMaterialReceipt()) {
			material.setAuditDetails(
					inventoryUtilityService.mapAuditDetails(openBalReq.getRequestInfo(), tenantId));
		}
		kafkaTemplate.send(updateTopic, openBalReq);
		return openBalReq.getMaterialReceipt();	
	}
	
	
	public OpeningBalanceResponse search(OpeningBalanceSearchCriteria request) {
		return openingBalanceRepository.search(request);

	}
	
	
	private String generateTargetNumber(String tenantId, RequestInfo requestInfo) {

		return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTargetNumPath);
	}
	
	private String appendString(MaterialReceipt headerRequest)
	{  
	    Calendar cal = Calendar.getInstance();
	    int year= cal.get(Calendar.YEAR);
		String code="MRN/";
			int id=	Integer.valueOf(openingBalanceRepository.getSequence(headerRequest));
			String idgen =String.format("%05d", id);
			String mrnNumber= code  + idgen +"/"+ year;
		return mrnNumber;
	}
	
	
	
}
