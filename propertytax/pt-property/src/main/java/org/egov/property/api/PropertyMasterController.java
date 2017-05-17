package org.egov.property.api;

import org.egov.property.model.MasterModel;
import org.egov.property.model.MasterResponse;
import org.egov.property.model.RequestInfo;
import org.egov.property.model.ResponseInfo;
import org.egov.property.services.Masterservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping(path="/property")

public class PropertyMasterController {

	@Autowired
	Masterservice masterService;
	
	@RequestMapping(path="/propertyType/_search",method=RequestMethod.POST)
	public MasterResponse getPropertyTypes(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {
		MasterModel	masterModel=	masterService.getPropertyTypes(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/apartmentMaster/_search",method=RequestMethod.POST)
	public MasterResponse getApartmentMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getApartmentMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/floorTypeMaster/_search",method=RequestMethod.POST)
	public MasterResponse getFloorTypeMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getFloorTypeMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/ocupancyMaster/_search",method=RequestMethod.POST)
	public MasterResponse getOcupancyMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getOcupancyMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/roofTypeMaster/_search",method=RequestMethod.POST)
	public MasterResponse getRoofTypeMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getRoofTypeMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	@RequestMapping(path="/taxRateMaster/_search",method=RequestMethod.POST)
	public MasterResponse getTaxRateMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getTaxRateMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	@RequestMapping(path="/wallTypeMaster/_search",method=RequestMethod.POST)
	public MasterResponse getWallTypeMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getWallTypeMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	@RequestMapping(path="/woodTypeMaster/_search",method=RequestMethod.POST)
	public MasterResponse getWoodTypeMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getWoodTypeMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/usageMaster/_search",method=RequestMethod.POST)
	public MasterResponse getUsageMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getUsageMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/structureMaster/_search",method=RequestMethod.POST)
	public MasterResponse getStructureMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getStructureMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/documentTypeMaster/_search",method=RequestMethod.POST)
	public MasterResponse getDocumentTypeMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getDocumentTypeMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/mutationReasonMaster/_search",method=RequestMethod.POST)
	public MasterResponse getMutationReasonMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getMutationReasonMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
	@RequestMapping(path="/mutationRateMaster/_search",method=RequestMethod.POST)
	public MasterResponse getMutationRateMaster(@RequestParam String tenantId,@RequestParam String code,@RequestBody RequestInfo requestInfo) throws Exception {

		MasterModel	masterModel=	masterService.getMutationRateMaster(tenantId, code);
		MasterResponse masterResponse=new MasterResponse();
		masterResponse.setMasterModel(masterModel);
		masterResponse.setResonseInfo(new ResponseInfo());
		return masterResponse;
	}
	
}
