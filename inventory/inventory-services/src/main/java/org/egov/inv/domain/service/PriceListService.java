package org.egov.inv.domain.service;

import java.util.Collections;
import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.exception.CustomBindException;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PriceListService extends DomainService {


    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";
    
    @Value("${inv.pricelists.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.save.key}")
    private String saveKey;

    @Value("${inv.pricelists.update.topic}")
    private String updateTopic;

    @Value("${inv.pricelists.update.key}")
    private String updateKey;
    
    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListJdbcRepository priceListJdbcRepository;
    
    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";
    
    public PriceListResponse save(PriceListRequest priceListRequest, String tenantId) {

        try {
        	
            List<String> priceListIdList = priceListJdbcRepository.getSequence(PriceList.class.getSimpleName(), priceListRequest.getPriceLists().size());

            priceListRequest.getPriceLists().forEach(priceList -> {
                priceList.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                priceList.getPriceListDetails().forEach(priceListDetail -> {
                	priceListDetail.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                });
            });
            
            for (int i = 0; i <= priceListIdList.size() - 1; i++) {
                priceListRequest.getPriceLists().get(i)
                        .setId(priceListIdList.get(i).toString());
                
                List<String> priceListDetailsIdList = priceListJdbcRepository.getSequence(PriceListDetails.class.getSimpleName(), priceListRequest.getPriceLists().get(i).getPriceListDetails().size());
                for(int j =0; j <= priceListDetailsIdList.size()-1; j++){
                	priceListRequest.getPriceLists().get(i)
    			            		.getPriceListDetails().get(j)
    			            		.setId(priceListDetailsIdList.get(j).toString());
                }
            }

            kafkaQue.send(saveTopic, saveKey, priceListRequest);

            PriceListResponse response = PriceListResponse.builder()
                    .priceLists(priceListRequest.getPriceLists())
                    .responseInfo(getResponseInfo(priceListRequest.getRequestInfo()))
                    .build();

            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

	public PriceListResponse update(PriceListRequest priceListRequest,
			String tenantId) {

		try {
			priceListRequest.getPriceLists().stream().forEach(priceList -> {
								priceList.setAuditDetails(mapAuditDetailsForUpdate(priceListRequest.getRequestInfo()));
							});

			kafkaQue.send(updateTopic, updateKey, priceListRequest);

			PriceListResponse response = new PriceListResponse();
			response.setResponseInfo(getResponseInfo(priceListRequest
					.getRequestInfo()));
			response.setPriceLists(priceListRequest.getPriceLists());
			
			return response;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	public PriceListResponse search(
			PriceListSearchRequest priceListSearchRequest, RequestInfo requestInfo) {

		Pagination<PriceList> searchPriceLists = priceListRepository.search(priceListSearchRequest);
		
        PriceListResponse response = new PriceListResponse();
        response.setPriceLists(searchPriceLists.getPagedData().size() > 0 ? searchPriceLists.getPagedData() : Collections.emptyList());
		return response;
	}

}
