package org.egov.pa.service;

import java.util.List;

import org.egov.pa.model.ULBKpiValueList;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.egov.pa.web.contract.ValueResponse;
import org.springframework.stereotype.Service;


@Service
public interface KpiValueService {

		
	/**
	 * This API is used to add an entry to KPI Value with Actual Value for KPI Target 
	 * API has to receive the request from Controller
	 * Set the Created Date and Created By Details
	 * Pass the request to repository for persistence
	 * @param kpiValueRequest
	 * @return KPIValueRequest
	 */
	public KPIValueRequest createKpiValue(KPIValueRequest kpiValueRequest);
	
	/**
	 * This API is used to updated the existing entry of Actual Value
	 * API has to receive the request from Controller. 
	 * Set the Updated Date and Updated By Details
	 * Pass the request to repository for persistence 
	 * @param kpiValueRequest
	 * @return
	 */
	public KPIValueRequest updateKpiValue(KPIValueRequest kpiValueRequest);
	
	/** 
	 * This API is used to search the existing records of Actual Values
	 * API has to receive the Get Request from Controller. 
	 * Pass the request to repository for retrieval 
	 * @param kpiValueSearchReq
	 * @return
	 */
	public List<ValueResponse> searchKpiValue(KPIValueSearchRequest kpiValueSearchReq);
	
	/** 
	 * This API is used to search the existing records of Actual Values
	 * API has to receive the Get Request from Controller. 
	 * Pass the request to repository for retrieval 
	 * @param kpiValueSearchReq
	 * @return
	 */
	public List<ULBKpiValueList> compareSearchKpiValue(KPIValueSearchRequest kpiValueSearchReq);
	
	
	
	
}
