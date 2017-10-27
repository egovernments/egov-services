package org.egov.inv.domain.service;

import org.egov.inv.domain.model.Pagination;
import org.egov.inv.domain.model.Store;
import org.egov.inv.domain.repository.StoreRepository;
import org.egov.inv.domain.service.validator.StoreRequestValidator;
import org.egov.inv.web.contract.StoreGetRequest;
import org.egov.inv.web.contract.StoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryUtilityService inventoryUtilityService;

    @Autowired
    private StoreRequestValidator storeRequestValidator;

    public List<Store> create(StoreRequest storeRequest, String tenantId) {
        storeRequest.stores.forEach(store -> {
            storeRequestValidator.validate(store, tenantId);
        });

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

        storeRequest.stores.forEach(store -> {
            storeRequestValidator.validate(store, tenantId);
        });
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
        return storeRepository.checkStoreCodeExists(code, tenantId);

    }


}