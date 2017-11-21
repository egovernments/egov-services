package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.egov.common.DomainService;
import org.egov.common.JdbcRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.MaterialReceipt.ReceiptTypeEnum;
import org.egov.inv.model.MaterialReceiptSearch;
import org.egov.inv.model.OpeningBalanceRequest;
import org.egov.inv.model.OpeningBalanceResponse;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpeningBalanceService extends DomainService {

    @Autowired
    private IdgenRepository idgenRepository;


    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    @Value("${inv.openbalance.save.topic}")
    private String createTopic;

    @Value("${inv.openbalance.update.topic}")
    private String updateTopic;

    @Value("${inv.openbal.idgen.name}")
    private String idGenNameForTargetNumPath;
    
    @Autowired
    private MaterialReceiptJdbcRepository jdbcRepository;
    

    @Autowired
    private MaterialReceiptService materialReceiptService;

  

    public List<MaterialReceipt> create(OpeningBalanceRequest openBalReq, String tenantId) {
    	try {
            openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
            materialReceipt.setId(jdbcRepository.getSequence("seq_materialreceipt"));
            materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.CREATED);
            if (isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }
            materialReceipt.setReceiptType(ReceiptTypeEnum.valueOf("OPENING_BALANCE"));
            String mrnNumber = appendString(materialReceipt);
            materialReceipt.setMrnNumber(mrnNumber);
            materialReceipt.getReceiptDetails().stream().forEach(detail -> {
                detail.setId(jdbcRepository.getSequence("seq_materialreceiptdetail"));
                if (isEmpty(detail.getTenantId())) {
                    detail.setTenantId(tenantId);
                }
                detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
                    addinfo.setId(jdbcRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
                    if (isEmpty(addinfo.getTenantId())) {
                        addinfo.setTenantId(tenantId);
                    }
                });
            });
        });
        for (MaterialReceipt material : openBalReq.getMaterialReceipt()) {
            material.setAuditDetails(
                    getAuditDetails(openBalReq.getRequestInfo(), "CREATE"));
            material.setId(jdbcRepository.getSequence(material));
        }
        kafkaTemplate.send(createTopic, openBalReq);
        return openBalReq.getMaterialReceipt();
    	} catch (CustomBindException e) {
            throw e;
        }
    }

    public List<MaterialReceipt> update(OpeningBalanceRequest openBalReq, String tenantId) {
    	try {
        openBalReq.getMaterialReceipt().stream().forEach(materialReceipt -> {
            if (isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }
            materialReceipt.getReceiptDetails().stream().forEach(detail -> {
                if (isEmpty(detail.getTenantId())) {
                    detail.setTenantId(tenantId);
                }
                detail.getReceiptDetailsAddnInfo().stream().forEach(addinfo -> {
                    if (isEmpty(addinfo.getTenantId())) {
                        addinfo.setTenantId(tenantId);
                    }
                });
            });
        });

        for (MaterialReceipt material : openBalReq.getMaterialReceipt()) {
            material.setAuditDetails(
                    getAuditDetails(openBalReq.getRequestInfo(), "UPDATE"));
        }
        kafkaTemplate.send(updateTopic, openBalReq);
        return openBalReq.getMaterialReceipt();
    	}
    	catch (CustomBindException e) {
            throw e;
        }
    }


    public OpeningBalanceResponse search(MaterialReceiptSearch materialReceiptSearch) {
        Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptService.search(materialReceiptSearch);
        OpeningBalanceResponse response = new OpeningBalanceResponse();
        return response
                .responseInfo(null)
                .materialReceipt(materialReceiptPagination.getPagedData().size() > 0 ? materialReceiptPagination.getPagedData() : Collections.EMPTY_LIST);
    }

    private String appendString(MaterialReceipt headerRequest) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "MRN/";
        int id = Integer.valueOf(jdbcRepository.getSequence(headerRequest));
        String idgen = String.format("%05d", id);
        String mrnNumber = code + idgen + "/" + year;
        return mrnNumber;
    }


}
