package org.egov.inv.domain.service;

import java.util.List;

import org.egov.inv.domain.exception.ErrorCode;
import org.egov.inv.domain.exception.InvalidDataException;
import org.egov.inv.domain.model.ReceiptNotesSearchCriteria;
import org.egov.inv.domain.model.SupplierGetRequest;
import org.egov.inv.persistence.entity.ReceiptNoteApiEntity;
import org.egov.inv.persistence.entity.SupplierEntity;
import org.egov.inv.persistence.repository.RecieptNoteApiJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import io.swagger.model.MaterialReceipt;
import io.swagger.model.MaterialReceiptRequest;
import io.swagger.model.Pagination;
import io.swagger.model.Supplier;
import io.swagger.model.SupplierRequest;

@Service
public class ReceiptNoteApiService {
	
	@Autowired
	private InventoryUtilityService inventoryUtilityService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private RecieptNoteApiJdbcRepository recieptNoteApiJdbcRepository;

	@Value("${inv.reciept.save.topic}")
	private String createTopic;

	@Value("${inv.reciept.update.topic}")
	private String updateTopic;
	
	public List<MaterialReceipt> create(MaterialReceiptRequest headerRequest, String tenantId) {

		for (MaterialReceipt material : headerRequest.getMaterialReceipt()) {
			material.setAuditDetails(
					inventoryUtilityService.mapAuditDetails(headerRequest.getRequestInfo(), tenantId));
			if (!recieptNoteApiJdbcRepository.uniqueCheck("id", new  ReceiptNoteApiEntity().toEntity(material))) {
				throw new InvalidDataException("inv.004", ErrorCode.MANDATORY_VALUE_MISSING.getCode(),material.getId());
			}
			material.setId(recieptNoteApiJdbcRepository.getSequence(material));
		}
		return push(headerRequest);
	}
	
	public List<MaterialReceipt> update(MaterialReceiptRequest headerRequest, String tenantId) {

		for (MaterialReceipt material : headerRequest.getMaterialReceipt()) {
			if (material.getId() == null) {
				throw new InvalidDataException("id", ErrorCode.MANDATORY_VALUE_MISSING.getCode(), material.getId());
			}
			material.setAuditDetails(
					inventoryUtilityService.mapAuditDetailsForUpdate(headerRequest.getRequestInfo(), tenantId));
			if (!recieptNoteApiJdbcRepository.uniqueCheck("id", new ReceiptNoteApiEntity().toEntity(material))) {
				throw new CustomException("inv.004", "reciept id and tenantId combination should be unique");
			}
		}

		return pushForUpdate(headerRequest);
	}
	
	public List<MaterialReceipt> pushForUpdate(MaterialReceiptRequest headerRequest) {
		kafkaTemplate.send(updateTopic, headerRequest);
		return headerRequest.getMaterialReceipt();
	}
	
	public List<MaterialReceipt> push(MaterialReceiptRequest headerRequest) {
		kafkaTemplate.send(createTopic, headerRequest);
		return headerRequest.getMaterialReceipt();
	}
	
	public Pagination<MaterialReceipt> search(ReceiptNotesSearchCriteria request) {
		return recieptNoteApiJdbcRepository.search(request);

	}
}
