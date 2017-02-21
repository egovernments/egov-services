package org.egov.eis.web.controller;

import java.util.Date;
import java.util.List;

import org.egov.eis.persistence.entity.PositionHierarchy;
import org.egov.eis.web.contract.Error;
import org.egov.eis.web.contract.ErrorRes;
import org.egov.eis.web.contract.PositionHierarchyRequest;
import org.egov.eis.web.contract.PositionHierarchyRes;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.domain.service.PositionHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionHierarchyRestController {

	@Autowired
	private PositionHierarchyService positionHierarchyService;

	@RequestMapping(value = "/positionhierarchies", method = RequestMethod.GET)
	@ResponseBody
	public PositionHierarchyRes getPositions(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "objectType") String objectType,
			@RequestParam(value = "objectSubType") String objectSubType,
			@RequestParam(value = "fromPosition") String fromPosition,
			@RequestParam(value = "toPosition", required = false) String toPosition,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "sort", required = false, defaultValue = "[-objectType]") List<String> sort)
			throws Exception {

		PositionHierarchyRes response = new PositionHierarchyRes();
		response.setResponseInfo(new ResponseInfo("", "", new Date().toString(), "", "", "Successful response"));
		if (objectType != null && !objectType.isEmpty() && objectSubType != null && !objectSubType.isEmpty()
				&& fromPosition != null && !fromPosition.isEmpty() && toPosition != null && !toPosition.isEmpty()) {
			response.getPositionHierarchy()
					.add(positionHierarchyService.getPosHirByFromAndToPosAndObjectTypeAndObjectSubType(
							Long.valueOf(fromPosition), Long.valueOf(toPosition), objectType, objectSubType));
		} else if (objectType != null && !objectType.isEmpty() && objectSubType != null && !objectSubType.isEmpty()
				&& fromPosition != null && !fromPosition.isEmpty() && (toPosition == null || toPosition.isEmpty())) {
			response.getPositionHierarchy().add(positionHierarchyService.getPosHirByPosAndObjectTypeAndObjectSubType(
					Long.valueOf(fromPosition), objectType, objectSubType));
		} else if (objectType != null && !objectType.isEmpty() && objectSubType != null && !objectSubType.isEmpty()
				&& ((fromPosition == null || fromPosition.isEmpty()) && (toPosition == null || toPosition.isEmpty()))) {
			response.getPositionHierarchy()
					.addAll(positionHierarchyService.getPosHirByObjectTypeAndObjectSubType(objectType, objectSubType));
		} else if (objectType != null && !objectType.isEmpty()
				&& ((objectSubType == null || objectSubType.isEmpty())
						&& (fromPosition == null || fromPosition.isEmpty())
						&& (toPosition == null || toPosition.isEmpty()))) {
			response.getPositionHierarchy()
					.addAll(positionHierarchyService.getListOfPositionHeirarchyByObjectType(objectType));
		} else {
			throw new Exception();
		}

		return response;
	}

	@GetMapping(value = "positionhierarchys")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute PositionHierarchyRequest positionHierarchyRequest) {

		PositionHierarchyRes response = new PositionHierarchyRes();
		List<PositionHierarchy> positionHierarchys = positionHierarchyService
				.getPositionHierarchys(positionHierarchyRequest);
		response.getPositionHierarchy().addAll(positionHierarchys);
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "");
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		response.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionHierarchyRes>(response, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "",
				"Failed to get positionHierarchys");
		response.setResponseInfo(responseInfo);
		//TODO: Fill right values
		Error error = new Error("", "", "");
//		error.setCode(400);
//		error.setDescription("Failed to get positionHierarchys");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}

}