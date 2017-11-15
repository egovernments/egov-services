package org.egov.inv.domain.service;

import io.swagger.model.Pagination;
import io.swagger.model.PriceList;
import io.swagger.model.PriceListRequest;
import io.swagger.model.PriceListSearchRequest;

import java.util.List;

import org.egov.inv.domain.repository.PriceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceListService {

    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";
    private PriceListRepository priceListRepository;
    private InventoryUtilityService inventoryUtilityService;


    @Autowired
    public PriceListService(PriceListRepository priceListRepository,
                           InventoryUtilityService inventoryUtilityService) {
        this.priceListRepository = priceListRepository;
        this.inventoryUtilityService = inventoryUtilityService;
    }

    public List<PriceList> save(PriceListRequest priceListRequest, String tenantId) {

        priceListRequest.getPriceLists().forEach(priceList -> {
            priceList.setAuditDetails(inventoryUtilityService.mapAuditDetails(priceListRequest.getRequestInfo(), tenantId));
            priceList.getPriceListDetails().forEach(priceListDetail -> {
            	priceListDetail.setAuditDetails(inventoryUtilityService.mapAuditDetails(priceListRequest.getRequestInfo(), tenantId));
            });
        });
        

        List<Long> priceListIdList = inventoryUtilityService.getIdList(priceListRequest.getPriceLists().size(), SEQ_PRICELIST);
        for (int i = 0; i <= priceListIdList.size() - 1; i++) {
            priceListRequest.getPriceLists().get(i)
                    .setId(priceListIdList.get(i).toString());
            
            List<Long> priceListDetailsIdList = inventoryUtilityService.getIdList(priceListRequest.getPriceLists().get(i).getPriceListDetails().size(), SEQ_PRICELISTDETAILS);
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
                    priceList.setAuditDetails(
                            inventoryUtilityService.mapAuditDetailsForUpdate(priceListRequest.getRequestInfo(), tenantId));
                });

        priceListRepository.update(priceListRequest);

        return priceListRequest.getPriceLists();
    }

    public Pagination<PriceList> search(PriceListSearchRequest priceListSearchRequest) {
        return priceListRepository.search(priceListSearchRequest);
    }

}
