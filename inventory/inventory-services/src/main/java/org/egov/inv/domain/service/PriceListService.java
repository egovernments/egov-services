package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceListService extends DomainService {

    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";
    private PriceListRepository priceListRepository;

    @Autowired
    PriceListJdbcRepository priceListJdbcRepository;
    
    @Autowired
    public PriceListService(PriceListRepository priceListRepository) {
        this.priceListRepository = priceListRepository;
    }

    public List<PriceList> save(PriceListRequest priceListRequest, String tenantId) {

        priceListRequest.getPriceLists().forEach(priceList -> {
            priceList.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
            priceList.getPriceListDetails().forEach(priceListDetail -> {
            	priceListDetail.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
            });
        });
        

        List<String> priceListIdList = priceListJdbcRepository.getSequence(PriceList.class.getSimpleName(), priceListRequest.getPriceLists().size());
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

        priceListRepository.save(priceListRequest);

        return priceListRequest.getPriceLists();
    }

    public List<PriceList> update(PriceListRequest priceListRequest, String tenantId) {

        priceListRequest.getPriceLists().stream()
                .forEach(priceList -> {
                    priceList.setAuditDetails(mapAuditDetailsForUpdate(priceListRequest.getRequestInfo()));
                });

        priceListRepository.update(priceListRequest);

        return priceListRequest.getPriceLists();
    }

    public Pagination<PriceList> search(PriceListSearchRequest priceListSearchRequest) {
        return priceListRepository.search(priceListSearchRequest);
    }

}
