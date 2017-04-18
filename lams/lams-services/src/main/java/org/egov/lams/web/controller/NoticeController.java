package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.service.NoticeService;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.NoticeResponse;
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
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> generateNotice(@RequestBody NoticeRequest noticeRequest,
											BindingResult errors) {
		
		LOGGER.info("NoticeController noticeRequest:"+noticeRequest);
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes,HttpStatus.BAD_REQUEST);
		}
		NoticeResponse noticeResponse = noticeService.generateNotice(noticeRequest);
		
		return new ResponseEntity<NoticeResponse>(noticeResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute NoticeCriteria noticeCriteria, BindingResult errors) {
		
		LOGGER.info("NoticeController noticeCriteria:"+noticeCriteria);
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes,HttpStatus.BAD_REQUEST);
		}
		NoticeResponse noticeResponse = new NoticeResponse();
		Notice notice = new Notice(); 
		List<Notice> notices = new ArrayList<Notice>();
		notices.add(notice);
		noticeResponse.setNotices(notices);
		return new ResponseEntity<NoticeResponse>(noticeResponse, HttpStatus.CREATED);
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
				error.getFields().put(errs.getField(),errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}
}
