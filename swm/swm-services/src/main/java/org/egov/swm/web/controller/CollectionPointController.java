package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.service.CollectionPointService;
import org.egov.swm.persistence.repository.JdbcRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.egov.swm.web.requests.CollectionPointResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectionpoints")
public class CollectionPointController {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionPointController.class);


    @Autowired
    private CollectionPointService collectionPointService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionPointResponse create(@RequestBody @Valid CollectionPointRequest collectionPointRequest) {

        collectionPointRequest = collectionPointService.create(collectionPointRequest);

        return CollectionPointResponse.builder().responseInfo(getResponseInfo(collectionPointRequest.getRequestInfo()))
                .collectionPoints(collectionPointRequest.getCollectionPoints()).build();
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionPointResponse update(@RequestBody @Valid CollectionPointRequest collectionPointRequest) {

        collectionPointRequest = collectionPointService.update(collectionPointRequest);

        return CollectionPointResponse.builder().responseInfo(getResponseInfo(collectionPointRequest.getRequestInfo()))
                .collectionPoints(collectionPointRequest.getCollectionPoints()).build();
    }

    @PostMapping("/_search")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CollectionPointResponse search(@ModelAttribute final CollectionPointSearch collectionPointSearch,
            @RequestBody final RequestInfo requestInfo, @RequestParam final String tenantId) {

        long start = System.currentTimeMillis();
        final Pagination<CollectionPoint> collectionPointList = collectionPointService.search(collectionPointSearch);

        long end = System.currentTimeMillis();

        LOG.info("Time taken for collectionPointService.search(collectionPointSearch) in controller " + (end - start) + "ms");

        return CollectionPointResponse.builder().responseInfo(getResponseInfo(requestInfo))
                .collectionPoints(collectionPointList.getPagedData()).page(new PaginationContract(collectionPointList))
                .build();

    }

    private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
        return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
                .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
    }

}