package org.egov.inv.domain.service;

import io.swagger.model.StoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    private InventoryUtilityService inventoryUtilityService;

    public void create(StoreRequest storeRequest, String tenantId) {
        List<Long> storesIdList = inventoryUtilityService.getIdList(storeRequest.getStores().size(), "seq_stores");
        for (int i = 0; i <= storeRequest.getStores().size() - 1; i++) {
            storeRequest.getStores().get(i)
                    .setId(storesIdList.get(i).toString());
            storeRequest.getStores().get(i).setAuditDetails(inventoryUtilityService.mapAuditDetails(storeRequest.getRequestInfo(), tenantId));
        }
    }

}
