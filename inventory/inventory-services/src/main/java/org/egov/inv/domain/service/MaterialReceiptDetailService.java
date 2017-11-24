package org.egov.inv.domain.service;

import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.MaterialReceiptAddInfoSearch;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.MaterialReceiptDetailAddnlinfo;
import org.egov.inv.model.MaterialReceiptDetailSearch;
import org.egov.inv.persistence.repository.MaterialReceiptDetailJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MaterialReceiptDetailService extends DomainService {

    @Autowired
    private MaterialReceiptDetailJdbcRepository materialReceiptDetailJdbcRepository;

    @Autowired
    private MaterialReceiptDetailAddInfoService materialReceiptDetailAddInfoService;

    public Pagination<MaterialReceiptDetail> search(MaterialReceiptDetailSearch materialReceiptDetailSearch) {
        Pagination<MaterialReceiptDetail> materialReceiptDetailPagination = materialReceiptDetailJdbcRepository.search(materialReceiptDetailSearch);

        List<MaterialReceiptDetail> receiptDetails = materialReceiptDetailPagination.getPagedData();

        if (receiptDetails.size() > 0) {
            for (MaterialReceiptDetail materialReceiptDetail : receiptDetails) {
                MaterialReceiptAddInfoSearch materialReceiptAddInfoSearch = MaterialReceiptAddInfoSearch.builder()
                        .receiptDetailId(Arrays.asList(materialReceiptDetail.getId()))
                        .tenantId(materialReceiptDetailSearch.getTenantId())
                        .build();
                Pagination<MaterialReceiptDetailAddnlinfo> detailAddnlinfoPagination = materialReceiptDetailAddInfoService.search(materialReceiptAddInfoSearch);
                materialReceiptDetail.setReceiptDetailsAddnInfo(detailAddnlinfoPagination.getPagedData());
            }
        }

        return materialReceiptDetailPagination;
    }
}
