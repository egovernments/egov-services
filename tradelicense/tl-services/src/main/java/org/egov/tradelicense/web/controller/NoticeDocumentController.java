package org.egov.tradelicense.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.NoticeDocumentSearchContract;
import org.egov.tl.commons.web.contract.NoticeDocumentSearchResponse;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.model.NoticeDocument;
import org.egov.tradelicense.domain.model.NoticeDocumentSearch;
import org.egov.tradelicense.domain.service.NoticeDocumentService;
import org.egov.tradelicense.web.contract.NoticeDocumentGetRequest;
import org.egov.tradelicense.web.contract.NoticeDocumentRequest;
import org.egov.tradelicense.web.contract.NoticeDocumentResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noticedocument")
public class NoticeDocumentController {

	@Autowired
	NoticeDocumentService noticeDocumentService;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@RequestMapping(path = "/v1/_create", method = RequestMethod.POST)
	public NoticeDocumentResponse createTradelicense(@Valid @RequestBody NoticeDocumentRequest noticeDocumentRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = noticeDocumentRequest.getRequestInfo();
		// check for field validation errors
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		final List<NoticeDocument> documents = noticeDocumentService.createNoticeDocument(noticeDocumentRequest);
		
	
		NoticeDocumentResponse noticeDocumentResponse = new NoticeDocumentResponse();
		noticeDocumentResponse.setResponseInfo(getResponseInfo(requestInfo));
		
		
		noticeDocumentResponse.setNoticeDocument(documents);

		return noticeDocumentResponse;
	}

	@RequestMapping(path = "/v1/_update", method = RequestMethod.POST)
	public NoticeDocumentResponse updateTradelicense(@Valid @RequestBody NoticeDocumentRequest noticeDocumentRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = noticeDocumentRequest.getRequestInfo();
		// check for field validation errors
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		final List<NoticeDocument> documents = noticeDocumentService.updateNoticeDocument(noticeDocumentRequest);
		NoticeDocumentResponse noticeDocumentResponse = new NoticeDocumentResponse();
		noticeDocumentResponse.setResponseInfo(getResponseInfo(requestInfo));
		noticeDocumentResponse.setNoticeDocument(documents);

		return noticeDocumentResponse;
	}

	@RequestMapping(path = "/v1/_search", method = RequestMethod.POST)
	public NoticeDocumentSearchResponse searchNoticeDocument(
			@Valid @ModelAttribute NoticeDocumentGetRequest noticeDocumentGetRequest, BindingResult errors,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult requestBodyBindingResult) throws Exception {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		if (requestBodyBindingResult.hasErrors())
			throw new CustomBindException(requestBodyBindingResult, requestInfo);

		List<NoticeDocumentSearch> noticeDocuments = noticeDocumentService.getNoticeDocuments(noticeDocumentGetRequest,
				requestInfo);
		
		List<NoticeDocumentSearchContract> noticeDocumentContracts = new  ArrayList<NoticeDocumentSearchContract>();
		ModelMapper mapper = new ModelMapper();
		NoticeDocumentSearchContract NoticeDocumentContract= null;
		for(NoticeDocumentSearch documentNotice : noticeDocuments){
			NoticeDocumentContract = new NoticeDocumentSearchContract();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			mapper.map(documentNotice, NoticeDocumentContract);
			noticeDocumentContracts.add(NoticeDocumentContract);
		}
		
		final NoticeDocumentSearchResponse response = new NoticeDocumentSearchResponse();
		response.setNoticeDocument(noticeDocumentContracts);
		response.setResponseInfo(getResponseInfo(requestInfo));
		return response;
	}

	// get responseinfo from requestinfo
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
	}
}
