package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.domain.util.InventoryUtilities;
import org.egov.inv.model.Fifo;
import org.egov.inv.model.FifoRequest;
import org.egov.inv.model.FifoResponse;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.FifoEntity;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueReceiptFifoLogicRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialIssueReceiptFifoLogic extends DomainService {
	private static final Logger LOG = LoggerFactory.getLogger(MaterialIssueReceiptFifoLogic.class);
	@Autowired
	private MaterialIssueReceiptFifoLogicRepository materialIssueReceiptFifoLogicRepository;
	
	@Autowired
	private MaterialIssuedFromReceiptJdbcRepository materialIssuedFromReceiptJdbcRepository;

	public List<FifoEntity> implementFifoLogic(Store store, Material material, Long issueDate, String tenantId) {
		return materialIssueReceiptFifoLogicRepository.implementFifoLogic(store, material, issueDate, tenantId);
	}

	public List<FifoEntity> implementFifoLogicForReturnMaterial(Store store, Material material, Long issueDate,
			String tenantId, String mrnNumber) {
		return materialIssueReceiptFifoLogicRepository.implementFifoLogicForReturnMaterial(store, material, issueDate,
				tenantId, mrnNumber);
	}

	public FifoResponse getTotalStockAsPerMaterial(FifoRequest fifoRequest) {
		Uom uom = null;
		Fifo fifo = fifoRequest.getFifo();
		List<FifoEntity> listOfEntities = new ArrayList<>();
		if (fifoRequest.getFifo().getUom() != null)
			uom = getUom(fifo.getTenantId(), fifo.getUom().getCode(), new RequestInfo());
		if (fifo.getMrnNumber() != null)
			listOfEntities = implementFifoLogicForReturnMaterial(fifo.getStore(), fifo.getMaterial(),
					fifo.getIssueDate(), fifo.getTenantId(), fifo.getMrnNumber());
		else
			listOfEntities = implementFifoLogic(fifo.getStore(), fifo.getMaterial(), fifo.getIssueDate(),
					fifo.getTenantId());
		BigDecimal availableQuantityInStock = BigDecimal.ZERO;
		if (!listOfEntities.isEmpty())
			for (FifoEntity fifoEntity : listOfEntities) {
				LOG.info("fifoEntity :" + fifoEntity);
				if(uom != null)
					availableQuantityInStock = availableQuantityInStock
							.add(InventoryUtilities.getQuantityInSelectedUom(BigDecimal.valueOf(fifoEntity.getBalance()),
									uom.getConversionFactor()));
				else 
					LOG.info("balance :" + fifoEntity.getBalance());
					availableQuantityInStock = availableQuantityInStock
					.add(BigDecimal.valueOf(fifoEntity.getBalance()));

			}
		FifoResponse fifoResponse = new FifoResponse();
		if(fifoRequest.getRequestInfo() != null)
		fifoResponse.setResponseInfo(getResponseInfo(fifoRequest.getRequestInfo()));
		fifoResponse.setStock(availableQuantityInStock);
		return fifoResponse;

	}

	public FifoResponse getUnitRate(FifoRequest fifoRequest) {
		List<FifoEntity> listOfEntities = new ArrayList<>();
		Uom uom = getUom(fifoRequest.getFifo().getTenantId(), fifoRequest.getFifo().getUom().getCode(),
				new RequestInfo());
		Fifo fifo = fifoRequest.getFifo();
		BigDecimal convertedQuantityIssued = BigDecimal.ZERO;
		if (fifo.getUserQuantityIssued() != null)
	    convertedQuantityIssued = InventoryUtilities.getQuantityInBaseUom(fifo.getUserQuantityIssued(),uom.getConversionFactor());
		 BigDecimal quantityIssued = convertedQuantityIssued;
		if (fifo.getMrnNumber() != null)
			listOfEntities = implementFifoLogicForReturnMaterial(fifo.getStore(), fifo.getMaterial(),
					fifo.getIssueDate(), fifo.getTenantId(), fifo.getMrnNumber());
		else
			listOfEntities = implementFifoLogic(fifo.getStore(), fifo.getMaterial(), fifo.getIssueDate(),
					fifo.getTenantId());
		if(fifoRequest.getFifo().getPurpose() != null && fifoRequest.getFifo().getIssueDetailId() != null)
		{
			if(fifoRequest.getFifo().getPurpose().equals("update")){
				Pagination<MaterialIssuedFromReceipt> mifrData = materialIssuedFromReceiptJdbcRepository.search(fifoRequest.getFifo().getIssueDetailId(), fifoRequest.getFifo().getTenantId());
				List<MaterialIssuedFromReceipt> materialIssuedFromReceipt = new ArrayList<>();
				if(mifrData != null)
				materialIssuedFromReceipt =	mifrData.getPagedData();
				for(FifoEntity fifoEntity :listOfEntities){
			    for(MaterialIssuedFromReceipt mifr :materialIssuedFromReceipt){
			    	if(mifr.getMaterialReceiptId().equals(fifoEntity.getReceiptId()) && 
			    			mifr.getMaterialReceiptDetail().getId().equals(fifoEntity.getReceiptDetailId())
			    			&& mifr.getMaterialReceiptDetailAddnlinfoId().equals(fifoEntity.getReceiptDetailAddnInfoId()))
			    		fifoEntity.setBalance(fifoEntity.getBalance() + Double.valueOf(mifr.getQuantity().toString())); 
			    }
				}
			}
				
		}
		BigDecimal value = BigDecimal.ZERO;
		if (!listOfEntities.isEmpty())
			for (FifoEntity fifoEntity : listOfEntities) {
				if (uom.getConversionFactor() != null && fifoEntity.getUnitRate() != null)
					if (BigDecimal.valueOf(fifoEntity.getBalance()).compareTo(convertedQuantityIssued) <= 0) {
						if (fifoEntity.getUnitRate() != null)
							value = 
									BigDecimal.valueOf(fifoEntity.getUnitRate())
									.multiply(BigDecimal.valueOf(fifoEntity.getBalance())).add(value);
						convertedQuantityIssued = convertedQuantityIssued.subtract(BigDecimal.valueOf(fifoEntity.getBalance()));
					} else {
						if (fifoEntity.getUnitRate() != null)
							value = convertedQuantityIssued
									.multiply(BigDecimal.valueOf(fifoEntity.getUnitRate()))

									.add(value);
						convertedQuantityIssued = convertedQuantityIssued.ZERO;
					}
				if (convertedQuantityIssued.equals(BigDecimal.ZERO))
					break;
			}
		FifoResponse fifoResponse = new FifoResponse();
		fifoResponse.setResponseInfo(getResponseInfo(fifoRequest.getRequestInfo()));
		fifoResponse.setUnitRate(value.divide(convertedQuantityIssued));
		fifoResponse.setValue(value);
		return fifoResponse;
	}

}
