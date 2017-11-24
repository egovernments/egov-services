package org.egov.inv.domain.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailSearch;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialReceiptService extends DomainService {

    @Autowired
    private MaterialReceiptJdbcRepository materialReceiptJdbcRepository;

    @Autowired
    private MaterialReceiptDetailService materialReceiptDetailService;

    public Pagination<MaterialReceipt> search(MaterialReceiptSearch materialReceiptSearch) {
        Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptJdbcRepository.search(materialReceiptSearch);

        if (materialReceiptPagination.getPagedData().size() > 0) {
            for (MaterialReceipt materialReceipt : materialReceiptPagination.getPagedData()) {
                List<MaterialReceiptDetail> materialReceiptDetail = getMaterialReceiptDetail(materialReceipt.getMrnNumber(), materialReceiptSearch.getTenantId());
                materialReceipt.setReceiptDetails(materialReceiptDetail);
            }
        }

        return materialReceiptPagination;
    }

    private List<MaterialReceiptDetail> getMaterialReceiptDetail(String mrnNumber, String tenantId) {
        MaterialReceiptDetailSearch materialReceiptDetailSearch = MaterialReceiptDetailSearch.builder()
                .mrnNumber(Arrays.asList(mrnNumber))
                .tenantId(tenantId)
                .build();
        Pagination<MaterialReceiptDetail> materialReceiptDetails = materialReceiptDetailService.search(materialReceiptDetailSearch);
        return materialReceiptDetails.getPagedData().size() > 0 ? materialReceiptDetails.getPagedData() : Collections.EMPTY_LIST;
    }
}