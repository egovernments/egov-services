package org.egov.id.api;


import org.egov.id.util.IdGenerationService;
import org.egov.id.util.ResponseInfoFactory;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdResponse;
import org.egov.models.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/id/")
public class IdGenerationController {

	@Autowired
	IdGenerationService idGenerationService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@RequestMapping(method=RequestMethod.POST, path="_create")
	public IdGenerationResponse generateIdResponse(@RequestBody IdGenerationRequest idGenReq) throws Exception{
		
		RequestInfo requestInfo = idGenReq.getRequestInfo();
		IdGenerationResponse idGenerationResponse = new IdGenerationResponse();
		IdResponse idResponse = new IdResponse();
		
		String generatedId = idGenerationService.generateId(idGenReq);
		idResponse.setId(generatedId);
		idGenerationResponse.setIdResponse(idResponse);
		idGenerationResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		
		return idGenerationResponse;
	}

}
