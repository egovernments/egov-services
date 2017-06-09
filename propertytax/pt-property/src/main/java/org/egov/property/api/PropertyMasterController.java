package org.egov.property.api;

import org.egov.models.DepartmentRequest;
import org.egov.models.DepartmentResponseInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.property.model.MasterModel;
import org.egov.property.model.MasterResponse;
import org.egov.property.services.Masterservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to master
 * @author Narendra
 *
 */


@RestController
@RequestMapping(path="/property")
public class PropertyMasterController {

	@Autowired
	Masterservice masterService;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	/**
	 *Description : This api for getting property types
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/propertytype/_search",method=RequestMethod.POST)
	public MasterResponse getPropertyTypes(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {
		MasterModel	masterModel=	masterService.getPropertyTypes(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting apartment master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/apartmentmaster/_search",method=RequestMethod.POST)
	public MasterResponse getApartmentMaster(@RequestParam String tenantId,@RequestParam(required=false)  String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getApartmentMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting floor master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponseInfo
	 * @throws Exception
	 */


	@RequestMapping(path="/floortypemaster/_search",method=RequestMethod.POST)
	public MasterResponse getFloorTypeMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getFloorTypeMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting floor ocupancy master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/ocupancymaster/_search",method=RequestMethod.POST)
	public MasterResponse getOcupancyMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getOcupancyMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting roof type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/rooftypemaster/_search",method=RequestMethod.POST)
	public MasterResponse getRoofTypeMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getRoofTypeMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting tax rate master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/taxratemaster/_search",method=RequestMethod.POST)
	public MasterResponse getTaxRateMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getTaxRateMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting wall type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */


	@RequestMapping(path="/walltypemaster/_search",method=RequestMethod.POST)
	public MasterResponse getWallTypeMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getWallTypeMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}


	/**
	 * Description : This api for getting wood type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/woodtypemaster/_search",method=RequestMethod.POST)
	public MasterResponse getWoodTypeMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getWoodTypeMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}


	/**
	 * Description : This api for getting usage type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/usagemaster/_search",method=RequestMethod.POST)
	public MasterResponse getUsageMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getUsageMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}

	/**
	 * Description : This api for getting strcture master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */


	@RequestMapping(path="/structuremaster/_search",method=RequestMethod.POST)
	public MasterResponse getStructureMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getStructureMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}


	/**
	 * Description : This api for getting document type master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/documenttypemaster/_search",method=RequestMethod.POST)
	public MasterResponse getDocumentTypeMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getDocumentTypeMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=new ResponseInfo();
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}


	/**
	 * Description : This api for getting mutation reason master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */

	@RequestMapping(path="/mutationreasonmaster/_search",method=RequestMethod.POST)
	public MasterResponse getMutationReasonMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getMutationReasonMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}


	/**
	 * Description : This api for getting mutation master details
	 * @param tenantId
	 * @param code
	 * @param requestInfo
	 * @return masterResponse
	 * @throws Exception
	 */
	@RequestMapping(path="/mutationratemaster/_search",method=RequestMethod.POST)
	public MasterResponse getMutationRateMaster(@RequestParam String tenantId,@RequestParam(required=false) String code,@RequestBody RequestInfoWrapper requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getMutationRateMaster(tenantId, code,requestInfo.getRequestInfo());
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		ResponseInfo responseInfo=responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(),true);
		masterResponse.setResonseInfo(responseInfo);
		return masterResponse;
	}
	
	@RequestMapping(path="/departments/_create",method=RequestMethod.POST)
	public DepartmentResponseInfo createDepartmentMaster(@RequestParam String tenantId, @RequestBody DepartmentRequest departmentRequest){
		
	return	masterService.createDepartmentMaster(tenantId,departmentRequest);
		
	}

}
