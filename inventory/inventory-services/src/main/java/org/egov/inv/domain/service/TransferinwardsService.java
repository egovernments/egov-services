package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptResponse;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.model.TransferInwardRequest;
import org.egov.inv.model.TransferInwardResponse;
import org.egov.inv.persistence.entity.PurchaseOrderDetailEntity;
import org.egov.inv.persistence.entity.PurchaseOrderEntity;
import org.egov.inv.persistence.repository.TransferInwardRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TransferinwardsService extends DomainService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private TransferInwardRepository transferInwardRepository;
	
	@Autowired
	private MaterialReceiptService materialReceiptService;
	
	@Value("${inv.transfer.inward.save.topic}")
	private String createTopic;

	@Value("${inv.transfer.inward.update.topic}")
	private String updateTopic;
	
    @Value("${inv.transfer.inward.save.key}")
    private String createTopicKey;

    @Value("${inv.transfer.inward.update.key}")
    private String updateTopicKey;
	
	public TransferInwardResponse create(TransferInwardRequest inwardRequest, String tenantId)
	{
		try{
			List<MaterialReceipt> inwards = inwardRequest.getTransferInwards();
	        validate(inwards, tenantId, Constants.ACTION_CREATE);
	        inwards.forEach(materialReceipt ->
	        {
	            materialReceipt.setId(transferInwardRepository.getSequence("seq_materialreceipt"));
	            materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.CREATED);
	            materialReceipt.setMrnNumber(appendString(materialReceipt));
	            materialReceipt.setAuditDetails(getAuditDetails(inwardRequest.getRequestInfo(), tenantId));
	            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
	                materialReceipt.setTenantId(tenantId);
	            }

	            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
	    	        materialReceiptDetail.setId(transferInwardRepository.getSequence("seq_materialreceiptdetail"));
	    	        materialReceiptDetail.setTenantId(tenantId);

	    	        materialReceiptDetail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
						addinfo.setId(transferInwardRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
						if (isEmpty(addinfo.getTenantId())) {
							addinfo.setTenantId(tenantId);
						}
					});
				
	            });
	        });
	        kafkaTemplate.send(createTopic, createTopicKey,inwardRequest);
	        TransferInwardResponse response = new TransferInwardResponse();
			response.setResponseInfo(null);
			response.setTransferInwards(inwardRequest.getTransferInwards());
			return response;
			
		}catch(CustomBindException e){
			throw e;
		}
	}
	
	public TransferInwardResponse update(TransferInwardRequest inwardsRequest, String tenantId)
	{
		try{
		    List<MaterialReceipt> inwards = inwardsRequest.getTransferInwards();
	        validate(inwards, tenantId, Constants.ACTION_UPDATE);
	        List<String> materialReceiptDetailIds = new ArrayList<>();
	        List<String> materialReceiptDetailAddlnInfoIds = new ArrayList<>();
	        inwards.forEach(materialReceipt ->
	        {
	            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
	                materialReceipt.setTenantId(tenantId);
	            }

	            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
	                if (isEmpty(materialReceiptDetail.getTenantId())) {
	                    materialReceiptDetail.setTenantId(tenantId);
	                }

	                materialReceiptDetailIds.add(materialReceiptDetail.getId());

	                materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
	                        materialReceiptDetailAddnlInfo -> {
	                            materialReceiptDetailAddlnInfoIds.add(materialReceiptDetailAddnlInfo.getId());

	                            if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
	                                materialReceiptDetailAddnlInfo.setTenantId(tenantId);
	                            }
	                        });
	                transferInwardRepository.markDeleted(materialReceiptDetailAddlnInfoIds, tenantId, "materialreceiptdetailaddnlinfo", "receiptdetailid", materialReceiptDetail.getId());

	                transferInwardRepository.markDeleted(materialReceiptDetailIds, tenantId, "materialreceiptdetail", "mrnNumber", materialReceipt.getMrnNumber());

	            });
	        });
			kafkaTemplate.send(updateTopic,updateTopicKey, inwardsRequest);
			TransferInwardResponse response = new TransferInwardResponse();
			response.setResponseInfo(null);
			response.setTransferInwards(inwardsRequest.getTransferInwards());
			return response;
		}catch(CustomBindException e){
			throw e;
		}
	}
	
	public TransferInwardResponse search(MaterialReceiptSearch transferinwardsSearch, String tenantId)
	{
		Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptService.search(transferinwardsSearch);
        TransferInwardResponse response = new TransferInwardResponse();
        return response.
        		responseInfo(null).
        		transferInwards(materialReceiptPagination.getPagedData().size() > 0 ? materialReceiptPagination.getPagedData() : Collections.EMPTY_LIST);		
	}
	
	private void validate(List<MaterialReceipt> materialReceipts, String tenantId, String method) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } 
                }

                break;

                case Constants.ACTION_UPDATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } 
                }

                break;
            }
        } catch (IllegalArgumentException e) {
        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;

    }
	private String appendString(MaterialReceipt materialReceipt) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "MRN/";
        int id = Integer.valueOf(transferInwardRepository.getSequence(materialReceipt));
        String idgen = String.format("%05d", id);
        String mrnNumber = code + idgen + "/" + year;
        return mrnNumber;
    }
	
}
