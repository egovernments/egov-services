package org.egov.works.services.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.services.domain.exception.CustomBindException;
import org.egov.works.services.domain.service.DocumentDetailsService;
import org.egov.works.services.web.contract.DocumentDetail;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.DocumentDetailResponse;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.egov.works.services.web.contract.DocumentDetailSearchRequest;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documentdetails")
public class DocumentDetailController {

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping
    @RequestMapping("/_create")
    public ResponseEntity<?> createDocuments(@Valid @RequestBody DocumentDetailRequest documentDetailRequest,
                                             BindingResult errors) throws Exception {

        RequestInfo requestInfo = documentDetailRequest.getRequestInfo();

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        documentDetailsService.validateDocuments(documentDetailRequest);
        final List<DocumentDetail> documents = documentDetailsService.createDocuments(documentDetailRequest);
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true));
        documentDetailResponse.setDocumentDetails(documents);
        return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);

    }

    @PostMapping
    @RequestMapping("/_update")
    public ResponseEntity<?> updateDocuments(@Valid @RequestBody DocumentDetailRequest documentDetailRequest,
                                             BindingResult errors) throws Exception {

        RequestInfo requestInfo = documentDetailRequest.getRequestInfo();

        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        documentDetailsService.validateDocuments(documentDetailRequest);
        final List<DocumentDetail> documents = documentDetailsService.updateDocuments(documentDetailRequest);
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true));
        documentDetailResponse.setDocumentDetails(documents);
        return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);

    }

    @PostMapping
    @RequestMapping("/_search")
    public ResponseEntity<?> searchDocuments(@ModelAttribute DocumentDetailSearchRequest documentDetailSearchRequest,
                                             @RequestBody RequestInfo requestInfo, BindingResult errors) {


        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }

        documentDetailsService.validateSearchDocuments(documentDetailSearchRequest);
        List<DocumentDetail> documents = documentDetailsService.searchDocuments(new DocumentDetailSearchCriteria().toDomain(documentDetailSearchRequest));
        DocumentDetailResponse documentDetailResponse = new DocumentDetailResponse();
        documentDetailResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo,true));
        documentDetailResponse.setDocumentDetails(documents);
        return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);
    }
}
