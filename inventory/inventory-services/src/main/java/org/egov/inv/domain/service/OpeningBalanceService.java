package org.egov.inv.domain.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.model.OpeningBalanceSearchCriteria;
import org.egov.inv.persistence.repository.OpeningBalanceRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpeningBalanceService extends DomainService{
	
	@Autowired
	private IdgenRepository idgenRepository;
	

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
					getAuditDetails(headerRequest.getRequestInfo(),"CREATE"));
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
					mapAuditDetails(openBalReq.getRequestInfo()));
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
