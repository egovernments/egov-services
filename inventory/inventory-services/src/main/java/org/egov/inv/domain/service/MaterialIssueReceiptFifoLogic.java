package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.DomainService;
import org.egov.inv.model.Fifo;
import org.egov.inv.model.FifoRequest;
import org.egov.inv.model.FifoResponse;
import org.egov.inv.model.Material;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.FifoEntity;
import org.egov.inv.persistence.repository.MaterialIssueReceiptFifoLogicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialIssueReceiptFifoLogic extends DomainService {
	@Autowired
	private MaterialIssueReceiptFifoLogicRepository materialIssueReceiptFifoLogicRepository;

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
		if (StringUtils.isNotBlank(fifoRequest.getFifo().getUom().getCode()))
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
				if (uom.getConversionFactor() != null && fifoEntity.getBalance() != null)
					availableQuantityInStock = availableQuantityInStock
							.add(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
									Double.valueOf(uom.getConversionFactor().toString()))));
			}
		FifoResponse fifoResponse = new FifoResponse();
		fifoResponse.setResponseInfo(getResponseInfo(fifoRequest.getRequestInfo()));
		fifoResponse.setStock(availableQuantityInStock);
		return fifoResponse;

	}

	public FifoResponse getUnitRate(FifoRequest fifoRequest) {
		List<FifoEntity> listOfEntities = new ArrayList<>();
		Uom uom = getUom(fifoRequest.getFifo().getTenantId(), fifoRequest.getFifo().getUom().getCode(),
				new RequestInfo());
		Fifo fifo = fifoRequest.getFifo();
		BigDecimal quantityIssued = BigDecimal.ZERO;
		if (fifo.getQuantityIssued() != null)
			quantityIssued = fifo.getQuantityIssued();
		if (fifo.getMrnNumber() != null)
			listOfEntities = implementFifoLogicForReturnMaterial(fifo.getStore(), fifo.getMaterial(),
					fifo.getIssueDate(), fifo.getTenantId(), fifo.getMrnNumber());
		else
			listOfEntities = implementFifoLogic(fifo.getStore(), fifo.getMaterial(), fifo.getIssueDate(),
					fifo.getTenantId());
		BigDecimal unitRate = BigDecimal.ZERO;
		if (!listOfEntities.isEmpty())
			for (FifoEntity fifoEntity : listOfEntities) {
				BigDecimal balance = BigDecimal.ZERO;
				if (uom.getConversionFactor() != null && fifoEntity.getBalance() != null)
					balance = BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
							Double.valueOf(uom.getConversionFactor().toString())));
				if (balance.compareTo(quantityIssued) <= 0) {
					if (fifoEntity.getUnitRate() != null)
						unitRate = BigDecimal.valueOf(fifoEntity.getUnitRate()).multiply(balance).add(unitRate);
					quantityIssued = quantityIssued.subtract(balance);
				} else {
					if (fifoEntity.getUnitRate() != null)
						unitRate = quantityIssued.multiply(BigDecimal.valueOf(fifoEntity.getUnitRate())).add(unitRate);
					quantityIssued = quantityIssued.ZERO;
				}
				if (quantityIssued.equals(BigDecimal.ZERO))
					break;
			}
		FifoResponse fifoResponse = new FifoResponse();
		fifoResponse.setResponseInfo(getResponseInfo(fifoRequest.getRequestInfo()));
		fifoResponse.setUnitRate(unitRate);
		return fifoResponse;

	}

}
