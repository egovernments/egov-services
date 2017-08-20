package org.egov.web.indexer.service;

import java.util.Date;

import org.egov.web.indexer.contract.Connection;
import org.egov.web.indexer.contract.ConnectionDetailsEs;
import org.egov.web.indexer.contract.ConnectionIndex;
import org.egov.web.indexer.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectionAdaptorService {
	
	public static final Logger logger = LoggerFactory.getLogger(ConnectionAdaptorService.class);
	
	@SuppressWarnings("deprecation")
	public ConnectionIndex indexOnCreate(WaterConnectionReq waterConnectionReq) { 
		
		ConnectionIndex index = new ConnectionIndex();
		ConnectionDetailsEs connDetails = new ConnectionDetailsEs();
		Connection conn = waterConnectionReq.getConnection();
		connDetails.setAcknowledgementNumber(conn.getAcknowledgementNumber());
		connDetails.setApplicationType(conn.getApplicationType());
		connDetails.setAssetIdentifier(conn.getAssetIdentifier());
		connDetails.setBillingType(conn.getBillingType());
		connDetails.setBplCardHolderName(conn.getBplCardHolderName());
		connDetails.setCreatedDate(new Date(conn.getCreatedDate()));
		connDetails.setCategoryId(conn.getCategoryId());
		connDetails.setCategoryType(conn.getCategoryType());
		connDetails.setConnectionStatus(conn.getConnectionStatus());
		connDetails.setConnectionType(conn.getConnectionType());
		connDetails.setConsumerNumber(conn.getConsumerNumber());
		connDetails.setDemandid(conn.getDemandid());
		connDetails.setDonationCharge(conn.getDonationCharge());
		connDetails.setEstimationNumber(conn.getEstimationNumber());
		connDetails.setExecutionDate(new Date(conn.getExecutionDate()));
		connDetails.setHscPipeSizeType(conn.getHscPipeSizeType());
		connDetails.setId(conn.getId());
		connDetails.setIsLegacy(conn.getIsLegacy());
		connDetails.setLegacyConsumerNumber(conn.getLegacyConsumerNumber());
		connDetails.setNoOfFlats(conn.getNoOfFlats());
		connDetails.setNumberOfPersons(conn.getNumberOfPersons());
		connDetails.setNumberOfTaps(conn.getNumberOfTaps());
		connDetails.setPipesizeId(conn.getPipesizeId());
		connDetails.setPropertyIdentifier(conn.getPropertyIdentifier());
		connDetails.setSourceType(conn.getSourceType());
		connDetails.setSourceTypeId(conn.getSourceTypeId());
		connDetails.setStateId(conn.getStateId());
		connDetails.setStatus(conn.getStatus());
		connDetails.setSumpCapacity(conn.getSumpCapacity());
		connDetails.setSupplyType(conn.getSupplyType());
		connDetails.setSupplyTypeId(conn.getSupplyTypeId());
		connDetails.setTenantId(conn.getTenantId());
		connDetails.setWaterTreatment(conn.getWaterTreatment());
		connDetails.setWaterTreatmentId(conn.getWaterTreatmentId());
		return index; 
		
	}

}
