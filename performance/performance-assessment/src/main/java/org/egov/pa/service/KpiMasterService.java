package org.egov.pa.service;

import java.util.List;

import org.egov.pa.model.DocumentTypeContract;
import org.egov.pa.model.KPI;
import org.egov.pa.web.contract.KPIGetRequest;
import org.egov.pa.web.contract.KPIRequest;
import org.springframework.stereotype.Service;

@Service
public interface KpiMasterService {
	
	/**
	 * This API is used to Create a New KPI Record 
	 * Adding Bulk KPIs are allowed in this API
	 * API has to receive the request from Controller, Generate the IDs for the objects
	 * Set the Created Date, Created By for each object
	 * And pass the request to repository for persistence process
	 * @param kpiRequest
	 * @return KPIRequest
	 */
	public KPIRequest createNewKpi(KPIRequest kpiRequest); 
	
	/**
	 * This API is used to Update an existing KPI Record
	 * Updating Bulk KPIs are allowed in this API
	 * API has to receive the update request from controller
	 * Set the Updated Date and Updated By for each object
	 * Pass the request to repository for persistence process
	 * @param kpiRequest
	 * @return KPIRequest
	 */
	public KPIRequest updateNewKpi(KPIRequest kpiRequest); 

	/**
	 * This API is used to Delete an existing KPI Record 
	 * Deleting Bulk KPIs are allowed in this API 
	 * API has to receive the delete request from controller
	 * Pass the request to repository for Database Action
	 * Delete will only be a soft delete 
	 * @param kpiRequest
	 * @return KPIRequest
	 */
	public KPIRequest deleteNewKpi(KPIRequest kpiRequest); 
	
	/**
	 * This API is used to Search the existing KPI Records along with their Targets and Documents
	 * Search can be based on KPI Code, Name or Financial Year. 
	 * @param kpiGetRequest
	 * @return List<KPI>
	 */
	public List<KPI> searchKpi(KPIGetRequest kpiGetRequest); 
	
	public Boolean getKpiType(String kpiCode, String tenantId); 
	
	public List<DocumentTypeContract> getDocumentForKpi(String kpiCode); 
	
	

}
