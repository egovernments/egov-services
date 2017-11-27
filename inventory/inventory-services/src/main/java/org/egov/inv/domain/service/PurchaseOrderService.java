package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderRequest;
import org.egov.inv.model.PurchaseOrderResponse;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {
		 
	@Autowired
	private PurchaseOrderJdbcRepository purchaseOrderRepository;
	@Value("${inv.purchaseorders.save.topic}")
	private String saveTopic;
	@Value("${inv.purchaseorders.save.key}")
	private String saveKey;

	@Value("${inv.purchaseorders.save.topic}")
	private String updateTopic;
	@Value("${inv.purchaseorders.save.key}")
	private String updateKey;
	
	@Value("${inv.purchaseorders.nonindent.save.topic}")
	private String saveNonIndentTopic;
	@Value("${inv.purchaseorders.nonindent.save.key}")
	private String saveNonIndentKey;

	@Value("${inv.purchaseorders.nonindent.save.topic}")
	private String updateNonIndentTopic;
	@Value("${inv.purchaseorders.nonindent.save.key}")
	private String updateNonIndentKey;
	

	@Transactional
	public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest) {

		try {
			List<PurchaseOrder> purchaseOrders = fetchRelated(purchaseOrderRequest.getPurchaseOrders());
			validate(purchaseOrders, Constants.ACTION_CREATE);
		    List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(),purchaseOrders.size());
		    int i=0;
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				purchaseOrder.setId(sequenceNos.get(i));
				//move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
				purchaseOrder.setPurchaseOrderNumber(sequenceNos.get(i));
			    i++;
			    int j=0;
			    purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
			    List<String> detailSequenceNos = purchaseOrderRepository.getSequence(PurchaseOrderDetail.class.getSimpleName(),purchaseOrder.getPurchaseOrderDetails().size()); 
			    for(PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails())
			    {
			    	 purchaseOrderDetail.setId(detailSequenceNos.get(j));
			    	 purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());
			    	 j++;
			    }
			}

			// TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
			// PUSH DATA TO KAFKA.

			/*if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
				if (purchaseOrders.get(0).getPurchaseType().toString()
						.equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
					kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
				else
					kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
			}*/
			kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
			PurchaseOrderResponse response = new PurchaseOrderResponse();
			response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
			response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}
	@Transactional
	public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest) {

		try {
			List<PurchaseOrder> purchaseOrder = fetchRelated(purchaseOrderRequest.getPurchaseOrders());
			validate(purchaseOrder, Constants.ACTION_UPDATE);

			// TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
			// PUSH DATA TO KAFKA.

			if (purchaseOrder.size() > 0 && purchaseOrder.get(0).getPurchaseType() != null) {
				if (purchaseOrder.get(0).getPurchaseType().toString()
						.equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
					kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
				else
					kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
			}

			PurchaseOrderResponse response = new PurchaseOrderResponse();
			response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
			response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}
	
	 
	public PurchaseOrderResponse search(PurchaseOrderSearch is) {
		PurchaseOrderResponse response=new PurchaseOrderResponse();
		 Pagination<PurchaseOrder> search = purchaseOrderRepository.search(is);
		 response.setPurchaseOrders(search.getPagedData());
		 response.setPage(getPage(search));
		 return response;
	
	}
	private void validate(List<PurchaseOrder> pos, String method) {

		try {
			switch (method) {

			case Constants.ACTION_CREATE: {
				if (pos == null) {
					throw new InvalidDataException("purchaseOrders", ErrorCode.NOT_NULL.getCode(), null);
				}
			}
				break;

			}
		} catch (IllegalArgumentException e) {

		}

	}

	public List<PurchaseOrder> fetchRelated(List<PurchaseOrder> pos) {
		for (PurchaseOrder po : pos) {
			// fetch related items
			
		}

		return pos;
	}

}