package org.egov.lams.web.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.lams.model.DefaultersInfo;
import org.egov.lams.model.DueNotice;
import org.egov.lams.model.DueNoticeCriteria;
import org.egov.lams.model.DueSearchCriteria;
import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.service.NoticeService;
import org.egov.lams.web.contract.DefaultersInfoResponse;
import org.egov.lams.web.contract.DueNoticeRequest;
import org.egov.lams.web.contract.DueNoticeResponse;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.Error;
import org.egov.lams.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agreement/notice")
public class NoticeController {

	public static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> generateNotice(@RequestBody NoticeRequest noticeRequest, BindingResult errors) {

		LOGGER.info("NoticeController noticeRequest:" + noticeRequest);
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		NoticeResponse noticeResponse = noticeService.generateNotice(noticeRequest);

		return new ResponseEntity<>(noticeResponse, HttpStatus.CREATED);
	}

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid NoticeCriteria noticeCriteria,
			@RequestBody @Valid RequestInfo requestInfo, BindingResult errors) {

		LOGGER.info("NoticeController noticeCriteria:" + noticeCriteria);
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		List<Notice> notices = noticeService.getNotices(noticeCriteria);
		return getSuccessResponse(notices, requestInfo);
	}
	
	/*
	 * API to fetch all the defaulters details upto current installment
	 */
	@PostMapping("_duenotice")
	@ResponseBody
	public ResponseEntity<?> generateDueNotice(@ModelAttribute @Valid DueSearchCriteria dueCriteria,
			@RequestBody @Valid RequestInfo requestInfo, BindingResult errors) {
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(dueCriteria.getTenantId())) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Set<DefaultersInfo> defaulterDetails = noticeService.generateDueNotice(dueCriteria, requestInfo);
		return getSuccessResponseForSearch(defaulterDetails, requestInfo);
	}

	@PostMapping("duenotice/_create")
	@ResponseBody
	public ResponseEntity<?> createDueNotice(@RequestBody @Valid DueNoticeRequest dueNoticeRequest,
			BindingResult errors) {
		RequestInfo requestInfo = dueNoticeRequest.getRequestInfo();
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} 
	
		List<DueNotice> dueNotices = noticeService.createDueNotice(dueNoticeRequest);
		return getSuccessResponseForCreate(dueNotices, requestInfo);
	}
	
	@PostMapping("duenotice/_search")
	@ResponseBody
	public ResponseEntity<?> searchDueNotice(@ModelAttribute @Valid DueNoticeCriteria noticeCriteria,
			@RequestBody @Valid RequestInfo requestInfo, BindingResult errors) {
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} 
		 if (StringUtils.isBlank(noticeCriteria.getTenantId())) {
		 ErrorResponse errRes = populateErrors(errors);
		 return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		 }
		List<DueNotice> dueNotices = noticeService.getAllDueNotices(noticeCriteria);
		return getSuccessResponseForCreate(dueNotices, requestInfo);
	}
	
	
	private ResponseEntity<?> getSuccessResponseForSearch(Set<DefaultersInfo> defaulterSet, RequestInfo requestInfo) {
		DefaultersInfoResponse defaultersInfoResponse = new DefaultersInfoResponse();
		defaultersInfoResponse.setDefaulters(defaulterSet);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		defaultersInfoResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(defaultersInfoResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		/*
		 * ResponseInfo responseInfo = new ResponseInfo();
		 * responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		 * responseInfo.setApi_id(""); errRes.setResponseInfo(responseInfo);
		 */
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

	private ResponseEntity<?> getSuccessResponse(List<Notice> notices, RequestInfo requestInfo) {

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		LOGGER.info("before returning from getsucces resposne ::" + responseInfo + "notices : " + notices);
		return new ResponseEntity<>(new NoticeResponse(responseInfo, notices), HttpStatus.OK);
	}
	private ResponseEntity<?> getSuccessResponseForCreate(List<DueNotice> notices, RequestInfo requestInfo) {

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		return new ResponseEntity<>(new DueNoticeResponse(responseInfo, notices), HttpStatus.OK);
	}
}
