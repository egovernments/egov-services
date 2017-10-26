package org.egov.inv.domain.service;

import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.inv.domain.model.AuditDetails;
import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.repository.StoreRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryUtilityService inventoryUtilityService;

    public List<Store> create(StoreRequest storeRequest, String tenantId) {
      
        List<Store> storesList = storeRequest.getStores();

        List<Long> storesIdList = inventoryUtilityService.getIdList(storeRequest.getStores().size(), "seq_stores");

        for (int i = 0; i <= storesList.size() - 1; i++) {
            storeRequest.getStores().get(i)
                    .setId(storesIdList.get(i).toString());
            storeRequest.getStores().get(i).setAuditDetails(inventoryUtilityService.mapAuditDetails(storeRequest.getRequestInfo(), tenantId));
        }
        return storeRepository.create(storeRequest);
    }

    public List<Store> update(StoreRequest storeRequest, String tenantId) {
        List<Store> storesList = storeRequest.getStores();
        for (int i = 0; i <= storesList.size() - 1; i++) {
            storeRequest.getStores().get(i).setAuditDetails(inventoryUtilityService.mapAuditDetailsForUpdate(storeRequest.getRequestInfo(), tenantId));
        }
        return storeRepository.update(storeRequest);
    }

    public Pagination<Store> search(StoreGetRequest storeGetRequest) {
       return storeRepository.search(storeGetRequest);
        
    }

    public boolean checkStoreCodeExists(String code, String tenantId) {
        return  storeRepository.checkStoreCodeExists(code,tenantId);
        
    }

   

   
}