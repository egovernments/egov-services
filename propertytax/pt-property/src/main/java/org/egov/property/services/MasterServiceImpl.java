package org.egov.property.services;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.models.RequestInfo;
import org.egov.property.exception.InvalidInputException;
import org.egov.property.model.MasterListModel;
import org.egov.property.model.MasterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description : MasterService interface implementation class 
 * @author Narendra
 *
 */

@Service
public class MasterServiceImpl  implements Masterservice{

	@Autowired
	private MasterListModel masterList;

	/**
	 *Description : This method for getting property types
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */
	@Override
	public MasterModel getPropertyTypes(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getPropertyType(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting usage master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public MasterModel getUsageMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getUsageMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting ocupancy master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getOcupancyMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getOccupancyMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting tax rate details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getTaxRateMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getTaxRateMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting wall type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getWallTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getWallTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting roof type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getRoofTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getRoofTypeMaster(),tenantId,code,requestInfo);
	}


	/**
	 *Description : This method for getting wood type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getWoodTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getWoodTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting apartment master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getApartmentMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getApartmentMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting floor master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getFloorTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getFloorTypeMaster(),tenantId,code,requestInfo);
	}

	/**
	 * Description : this method will get data from yaml
	 * @param masterData
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return
	 */

	public MasterModel getMaster(List<MasterModel> masterData,String tenantId,String code,RequestInfo requestInfo){
		List<MasterModel> modelList = null;
		if(tenantId!=null && code!=null){
		 modelList= masterData.stream().filter(m->m.getTenantId().equalsIgnoreCase(tenantId)&& m.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
		}
		else if(tenantId==null || tenantId.isEmpty()){
			 modelList= masterData.stream().filter(m->m.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
		}
		else if(code==null || code.isEmpty()){
			 modelList= masterData.stream().filter(m->m.getTenantId().equalsIgnoreCase(tenantId)).collect(Collectors.toList());
		}
		if(modelList.size()==0)
			throw new InvalidInputException(requestInfo);
		return modelList.get(0);
	}

	/**
	 *Description : This method for getting strcture master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getStructureMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getStructureMaster(),tenantId,code,requestInfo);
	}

	/**
	 * Description : This method for getting usage mutation reason master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getMutationReasonMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getMutationReasonMaster(),tenantId,code,requestInfo);
	}


	/**
	 * Description : This method for getting mutation rate master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */


	@Override
	public MasterModel getMutationRateMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getMutationRateMaster(),tenantId,code,requestInfo);
	}

	/**
	 *Description : This method for getting document type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterModel
	 * @throws Exception
	 */

	@Override
	public MasterModel getDocumentTypeMaster(String tenantId, String code,RequestInfo requestInfo) throws Exception {
		return	getMaster(masterList.getDocumentTypeMaster(),tenantId,code,requestInfo);
	}

}
