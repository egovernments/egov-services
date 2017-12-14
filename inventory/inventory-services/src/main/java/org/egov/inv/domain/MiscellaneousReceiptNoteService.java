package org.egov.inv.domain;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.domain.service.MaterialReceiptService;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.entity.MaterialReceiptEntity;
import org.egov.inv.persistence.repository.ReceiptNoteRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MiscellaneousReceiptNoteService extends DomainService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Value("${inv.miscellaneousreceiptnote.save.topic}")
    private String saveTopic;

    @Value("${inv.miscellaneousreceiptnote.save.key}")
    private String saveTopicKey;

    @Autowired
    private MaterialReceiptService materialReceiptService;

    @Autowired
    private ReceiptNoteRepository receiptNoteRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    public MaterialReceiptResponse create(MaterialReceiptRequest materialReceiptRequest, String tenantId) {
        List<MaterialReceipt> materialReceipts = materialReceiptRequest.getMaterialReceipt();
        MaterialReceiptRequest fetchRelated = fetchRelated(materialReceiptRequest, tenantId);
        validate(fetchRelated.getMaterialReceipt(), tenantId, Constants.ACTION_CREATE);
        materialReceipts.forEach(materialReceipt ->
        {
            materialReceipt.setId(receiptNoteRepository.getSequence("seq_materialreceipt"));
            materialReceipt.setMrnNumber(appendString(materialReceipt));
            materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.CREATED);
            materialReceipt.setReceiptType(MaterialReceipt.ReceiptTypeEnum.MISCELLANEOUS_RECEIPT);
            materialReceipt.setAuditDetails(getAuditDetails(materialReceiptRequest.getRequestInfo(), tenantId));
            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }

            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
                setMaterialDetails(tenantId, materialReceiptDetail);
            });
        });

        logAwareKafkaTemplate.send(saveTopic, saveTopicKey, materialReceiptRequest);

        MaterialReceiptResponse materialReceiptResponse = new MaterialReceiptResponse();

        return materialReceiptResponse.responseInfo(null)
                .materialReceipt(materialReceipts);
    }

    public MaterialReceiptResponse update(MaterialReceiptRequest materialReceiptRequest, String tenantId) {
        List<MaterialReceipt> materialReceipts = materialReceiptRequest.getMaterialReceipt();
        MaterialReceiptRequest fetchRelated = fetchRelated(materialReceiptRequest, tenantId);
        validate(fetchRelated.getMaterialReceipt(), tenantId, Constants.ACTION_UPDATE);
        List<String> materialReceiptDetailIds = new ArrayList<>();
        List<String> materialReceiptDetailAddlnInfoIds = new ArrayList<>();
        materialReceipts.forEach(materialReceipt ->
        {
            materialReceipt.setReceiptType(MaterialReceipt.ReceiptTypeEnum.MISCELLANEOUS_RECEIPT);

            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }

            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
                if (isEmpty(materialReceiptDetail.getTenantId())) {
                    materialReceiptDetail.setTenantId(tenantId);
                }

                if (isEmpty(materialReceiptDetail.getId())) {
                    setMaterialDetails(tenantId, materialReceiptDetail);
                }

                materialReceiptDetailIds.add(materialReceiptDetail.getId());

                materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
                        materialReceiptDetailAddnlInfo -> {
                            materialReceiptDetailAddlnInfoIds.add(materialReceiptDetailAddnlInfo.getId());

                            if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
                                materialReceiptDetailAddnlInfo.setTenantId(tenantId);
                            }
                        }
                );
                receiptNoteRepository.markDeleted(materialReceiptDetailAddlnInfoIds, tenantId, "materialreceiptdetailaddnlinfo", "receiptdetailid", materialReceiptDetail.getId());

                receiptNoteRepository.markDeleted(materialReceiptDetailIds, tenantId, "materialreceiptdetail", "mrnNumber", materialReceipt.getMrnNumber());

            });
        });

        logAwareKafkaTemplate.send(saveTopic, saveTopicKey, materialReceiptRequest);

        MaterialReceiptResponse materialReceiptResponse = new MaterialReceiptResponse();

        return materialReceiptResponse.responseInfo(null)
                .materialReceipt(materialReceipts);
    }


    public MaterialReceiptResponse search(MaterialReceiptSearch materialReceiptSearch) {
        Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptService.search(materialReceiptSearch);
        MaterialReceiptResponse response = new MaterialReceiptResponse();
        return response
                .responseInfo(null)
                .materialReceipt(materialReceiptPagination.getPagedData().size() > 0 ? materialReceiptPagination.getPagedData() : Collections.EMPTY_LIST);
    }

    private void setMaterialDetails(String tenantId, MaterialReceiptDetail materialReceiptDetail) {
        materialReceiptDetail.setId(receiptNoteRepository.getSequence("seq_materialreceiptdetail"));
        if (isEmpty(materialReceiptDetail.getTenantId())) {
            materialReceiptDetail.setTenantId(tenantId);
        }

        materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
                materialReceiptDetailAddnlInfo -> {
                    materialReceiptDetailAddnlInfo.setId(receiptNoteRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
                    if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
                        materialReceiptDetailAddnlInfo.setTenantId(tenantId);
                    }
                }
        );
    }


    private void validate(List<MaterialReceipt> materialReceipts, String tenantId, String method) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }

                break;

                case Constants.ACTION_UPDATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }

                break;
            }
            for (MaterialReceipt materialReceipt : materialReceipts) {
                validateReceiptPurpose(materialReceipt.getMrnNumber(), materialReceipt.getReceiptPurpose().toString(),
                        tenantId, errors);
                validateIssue(tenantId, errors, materialReceipt.getIssueNumber());
                validateReceiptdate(errors, materialReceipt);
                for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
                    int j = 0;
                    validateNonIssueQuantity(tenantId, materialReceiptDetail, errors, j);
                    validateReceivedQuantity(errors, materialReceiptDetail, j);
                    j++;
                }
            }
        } catch (IllegalArgumentException e) {
        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;
    }

    private void validateReceiptdate(InvalidDataException errors, MaterialReceipt materialReceipt) {
        if (null != materialReceipt.getReceiptDate() && materialReceipt.getReceiptDate() > getCurrentDate()) {
			String date = convertEpochtoDate(materialReceipt.getReceiptDate());
            errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "Receipt date ",date);
        }
    }

    private void validateReceivedQuantity(InvalidDataException errors, MaterialReceiptDetail materialReceiptDetail, int j) {
        if (materialReceiptDetail.getReceivedQty().longValue() <= 0) {
            errors.addDataError(ErrorCode.RCVED_QTY_GT_ZERO.getCode(), String.valueOf(j));
        }
    }

    private void validateIssue(String tenantId, InvalidDataException errors, String issueNumber) {
        MaterialIssueEntity materialIssueEntity = new MaterialIssueEntity();
        materialIssueEntity.setIssueNumber(issueNumber);
        materialIssueEntity.setTenantId(tenantId);
        Object materialIssue = receiptNoteRepository.findById(materialIssueEntity, "MaterialIssueEntity");
        if (null == materialIssue) {
            errors.addDataError(ErrorCode.OBJECT_NOT_FOUND.getCode(), "Issue", "", issueNumber);
        }
    }

    private void validateNonIssueQuantity(String tenantId, MaterialReceiptDetail materialReceiptDetail, InvalidDataException errors, int row) {
        if (materialReceiptDetail.getAcceptedQty().longValue() > materialReceiptDetail.getReceivedQty().longValue()) {
            errors.addDataError(ErrorCode.QTY_LE_SCND_ROW.getCode(), "Issued Quantity", "Received Quantity", String.valueOf(row));
        }
    }

    private void validateReceiptPurpose(String mrnNumber, String receiptPurpose, String tenantId, InvalidDataException errors) {
        if (!isEmpty(mrnNumber)) {
            MaterialReceiptEntity materialReceiptEntity = new MaterialReceiptEntity();
            materialReceiptEntity.setMrnNumber(mrnNumber);
            materialReceiptEntity.setTenantId(tenantId);
            Object receiptEntity = receiptNoteRepository.findById(materialReceiptEntity, "MaterialReceiptEntity");
            MaterialReceiptEntity receiptEntityfromDb = (MaterialReceiptEntity) receiptEntity;
            if (null != receiptEntityfromDb) {
                if (!(receiptPurpose.toString().equalsIgnoreCase(receiptEntityfromDb.getReceiptPurpose().toString()))) {
                    errors.addDataError(ErrorCode.DOESNT_MATCH.getCode(), receiptPurpose, "receipt purpose", receiptEntityfromDb.getReceiptPurpose().toString());
                }
            }
        }
    }

    private String appendString(MaterialReceipt materialReceipt) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "MRN/";
        int id = Integer.valueOf(receiptNoteRepository.getSequence(materialReceipt));
        String idgen = String.format("%05d", id);
        String mrnNumber = code + idgen + "/" + year;
        return mrnNumber;
    }

    private MaterialReceiptRequest fetchRelated(MaterialReceiptRequest materialReceiptRequest, String tenantId) {

        List<MaterialReceipt> materialReceipts = materialReceiptRequest.getMaterialReceipt();

        for (MaterialReceipt materialReceipt : materialReceipts) {
            for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {

                Material material = (Material) mdmsRepository.fetchObject(tenantId, "inventory", "Material", "code", materialReceiptDetail.getMaterial().getCode(), Material.class);
                materialReceiptDetail.setMaterial(material);

                Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", materialReceiptDetail.getUom().getCode(), Uom.class);
                materialReceiptDetail.setUom(uom);

                if (null != materialReceiptDetail.getAcceptedQty() && null != uom.getConversionFactor()) {
                    Double convertedAcceptedQuantity = getSaveConvertedQuantity(materialReceiptDetail.getAcceptedQty().doubleValue(), uom.getConversionFactor().doubleValue());
                    materialReceiptDetail.setAcceptedQty(BigDecimal.valueOf(convertedAcceptedQuantity));
                }

                if (null != materialReceiptDetail.getReceivedQty() && null != uom.getConversionFactor()) {
                    Double convertedReceivedQuantity = getSaveConvertedQuantity(materialReceiptDetail.getReceivedQty().doubleValue(), uom.getConversionFactor().doubleValue());
                    materialReceiptDetail.setReceivedQty(BigDecimal.valueOf(convertedReceivedQuantity));
                }

                if (null != materialReceiptDetail.getUnitRate() && null != uom.getConversionFactor()) {
                    Double convertedRate = getSaveConvertedRate(materialReceiptDetail.getUnitRate().doubleValue(),
                            uom.getConversionFactor().doubleValue());
                    materialReceiptDetail.setUnitRate((BigDecimal.valueOf(convertedRate)));
                }
            }
        }

        return materialReceiptRequest;
    }

    private Long getCurrentDate() {
        return currentEpochWithoutTime() + (24 * 60 * 60 * 1000) - 1;
    }
    
    private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }

}
