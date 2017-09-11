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
		connDetails.setCreatedDate(new Date(Long.parseLong(conn.getCreatedDate())));
		connDetails.setCategoryId(conn.getCategoryId());
		connDetails.setCategoryType(conn.getCategoryType());
		connDetails.setConnectionStatus(conn.getConnectionStatus());
		connDetails.setConnectionType(conn.getConnectionType());
		connDetails.setHscPipeSizeType(conn.getHscPipeSizeType());
		connDetails.setId(conn.getId());
		connDetails.setIsLegacy(conn.getIsLegacy());
		if(conn.getIsLegacy()) { 
			connDetails.setLegacyConsumerNumber(conn.getLegacyConsumerNumber());
		}
		connDetails.setNoOfFlats(conn.getNoOfFlats());
		connDetails.setNumberOfPersons(conn.getNumberOfPersons());
		connDetails.setNumberOfTaps(conn.getNumberOfTaps());
		connDetails.setPipesizeId(conn.getPipesizeId());
		connDetails.setPropertyIdentifier(conn.getPropertyIdentifier());
		connDetails.setSourceType(conn.getSourceType());
		 // connDetails.setStatus(conn.getStatus());
		connDetails.setSumpCapacity(conn.getSumpCapacity());
		connDetails.setSupplyType(conn.getSupplyType());
		connDetails.setSupplyTypeId(conn.getSupplyTypeId());
		connDetails.setTenantId(conn.getTenantId());
		connDetails.setWaterTreatment(conn.getWaterTreatment());
		connDetails.setWaterTreatmentId(conn.getWaterTreatmentId());
		index.setConnectionDetails(connDetails);
		return index; 
		
	}

}
