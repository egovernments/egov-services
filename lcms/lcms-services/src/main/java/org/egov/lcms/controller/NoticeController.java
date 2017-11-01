package org.egov.lcms.controller;

import javax.validation.Valid;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.NoticeRequest;
import org.egov.lcms.models.NoticeResponse;
import org.egov.lcms.models.NoticeSearchCriteria;
import org.egov.lcms.models.NoticeSearchResponse;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.NoticeService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/legalcase/notice/")
public class NoticeController {

	@Autowired
	NoticeService noticeService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "_create")
	public ResponseEntity<?> createNotice(@RequestBody @Valid NoticeRequest noticeRequest) throws Exception {

		NoticeResponse noticeResponse = noticeService.createNotice(noticeRequest);
		return new ResponseEntity<>(noticeResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_update")
	public ResponseEntity<?> updateNotice(@RequestBody @Valid NoticeRequest noticeRequest) throws Exception {

		NoticeResponse noticeResponse = noticeService.updateNotice(noticeRequest);
		return new ResponseEntity<>(noticeResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_search")
	public ResponseEntity<?> searchNotice(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid NoticeSearchCriteria noticeSearchCriteria, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new CustomException(propertiesManager.getInvalidTenantCode(),
					propertiesManager.getExceptionMessage());
		}
		NoticeSearchResponse noticeSearchResponse = noticeService.searchNotice(noticeSearchCriteria,
				requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(noticeSearchResponse, HttpStatus.CREATED);
	}
}
