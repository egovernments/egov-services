package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialReceiptAddInfoSearch;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;
import org.egov.inv.persistence.repository.MaterialReceiptDetailAddInfoJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialReceiptDetailAddInfoService extends DomainService {

    @Autowired
    private MaterialReceiptDetailAddInfoJdbcRepository receiptDetailAddInfoJdbcRepository;

    public Pagination<MaterialReceiptDetailAddnlinfo> search(MaterialReceiptAddInfoSearch receiptAddInfoSearch) {
        return receiptDetailAddInfoJdbcRepository.search(receiptAddInfoSearch);
    }
}
