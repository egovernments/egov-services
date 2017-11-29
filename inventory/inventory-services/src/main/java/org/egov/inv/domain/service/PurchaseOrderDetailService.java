package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderDetailSearch;
import org.egov.inv.persistence.repository.PurchaseOrderDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderDetailService extends DomainService {


    @Autowired
    private PurchaseOrderDetailJdbcRepository purchaseOrderDetailJdbcRepository;

    public Pagination<PurchaseOrderDetail> search(PurchaseOrderDetailSearch purchaseOrderDetailSearch) {

        return purchaseOrderDetailJdbcRepository.search(purchaseOrderDetailSearch);

    }

}