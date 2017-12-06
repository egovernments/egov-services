package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Uom;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.model.MaterialIssue.MaterialIssueStatusEnum;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.MaterialIssueDetailsJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptsJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NonIndentMaterialIssueService extends DomainService{

	@Autowired
	private MaterialIssueJdbcRepository materialIssueJdbcRepository;

	@Autowired
	private MaterialIssueDetailsJdbcRepository materialIssueDetailsJdbcRepository;

	@Autowired
	private MaterialIssuedFromReceiptsJdbcRepository materialIssuedFromReceiptsJdbcRepository;

	@Value("${inv.issues.save.topic}")
	private String createTopic;

	@Value("${inv.issues.save.key}")
	private String createKey;

	@Value("${inv.issues.update.topic}")
	private String updateTopic;

	@Value("${inv.issues.update.key}")
	private String updateKey;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public MaterialIssueResponse create(MaterialIssueRequest nonIndentIssueRequest) {
		try {
			validate(nonIndentIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					nonIndentIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : nonIndentIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				setMaterialIssueValues(materialIssue,seqNo, Constants.ACTION_CREATE);
				materialIssue.setAuditDetails(mapAuditDetails(nonIndentIssueRequest.getRequestInfo()));
				i++;
				int j = 0;
				if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
					List<String> detailSequenceNos = materialIssueDetailsJdbcRepository.getSequence(
							MaterialIssueDetail.class.getSimpleName(), materialIssue.getMaterialIssueDetails().size());
					for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
						materialIssueDetail.setId(detailSequenceNos.get(j));
						materialIssueDetail.setTenantId(materialIssue.getTenantId());
						convertToUom(materialIssueDetail);
						j++;
						int k = 0;
						List<String> materialIssuedFromReceiptsSeqNos = materialIssuedFromReceiptsJdbcRepository
								.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(),
										materialIssueDetail.getMaterialIssuedFromReceipts().size());
						for (MaterialIssuedFromReceipt materialIssuedFromReceipts : materialIssueDetail
								.getMaterialIssuedFromReceipts()) {
							materialIssuedFromReceipts.setId(materialIssuedFromReceiptsSeqNos.get(k));
							materialIssuedFromReceipts.setTenantId(materialIssueDetail.getTenantId());
							k++;
						}
					}
				}
			}
			kafkaTemplate.send(createTopic, createKey, nonIndentIssueRequest);
			MaterialIssueResponse response = new MaterialIssueResponse();
			response.setMaterialIssues(nonIndentIssueRequest.getMaterialIssues());
			response.setResponseInfo(getResponseInfo(nonIndentIssueRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	private void validate(List<MaterialIssue> materialIssues, String method) {
		try {
			switch (method) {
			case "create":
				if (materialIssues == null) {
					throw new InvalidDataException("materialIssues", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (MaterialIssue materialIssue : materialIssues) {
					if (!materialIssueJdbcRepository.uniqueCheck("",
							new MaterialIssueEntity().toEntity(materialIssue , IssueTypeEnum.NONINDENTISSUE.toString()))) {

					}
				}
				break;
			case "update":
				for (MaterialIssue materialIssue : materialIssues) {
					if (StringUtils.isEmpty(materialIssue.getIssueNumber()))
						throw new CustomBindException("Issue Number is not provided");
				}
			default:

			}
		} catch (IllegalArgumentException e) {
		}

	}
	
	private void setMaterialIssueValues(MaterialIssue materialIssue, String seqNo, String action) {
		materialIssue.setIssueType(IssueTypeEnum.NONINDENTISSUE);
		if(action.equals(Constants.ACTION_CREATE)){
			int year = Calendar.getInstance().get(Calendar.YEAR);
		materialIssue.setIssueNumber("MRIN-" + String.valueOf(year) + "-" + seqNo);
		materialIssue.setMaterialIssueStatus(MaterialIssueStatusEnum.CREATED);
		}
		

	}
	
	private void convertToUom(MaterialIssueDetail materialIssueDetail) {
		Double quantityIssued = 0d;
		if (materialIssueDetail.getUom() != null && StringUtils.isNotEmpty(materialIssueDetail.getUom().getCode())) {
			Uom uom = getUom(materialIssueDetail.getTenantId(), materialIssueDetail.getUom().getCode(),
					new RequestInfo());
			if (materialIssueDetail.getQuantityIssued() != null)
				quantityIssued = getSaveConvertedQuantity(
						Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
						Double.valueOf(uom.getConversionFactor().toString()));
			materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
		}
	}


		public MaterialIssueResponse update(final MaterialIssueRequest materialIssueRequest, String tenantId) {
			validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_UPDATE);
			List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
			for (MaterialIssue materialIssue : materialIssues) {
				List<String> materialIssueDetailsIds = new ArrayList<>();
				if (StringUtils.isEmpty(materialIssue.getTenantId()))
					materialIssue.setTenantId(tenantId);
				setMaterialIssueValues(materialIssue, null, Constants.ACTION_UPDATE);
				materialIssue
						.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
				for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
					List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
					if (StringUtils.isEmpty(materialIssueDetail.getTenantId()))
						materialIssueDetail.setTenantId(tenantId);
					convertToUom(materialIssueDetail);
					if (StringUtils.isEmpty(materialIssueDetail.getId()))
						materialIssueDetail.setId(materialIssueDetailsJdbcRepository
								.getSequence(MaterialIssueDetail.class.getSimpleName(), 1).get(0));
					materialIssueDetailsIds.add(materialIssueDetail.getId());
					for (MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()) {
						if (StringUtils.isEmpty(mifr.getTenantId()))
							mifr.setTenantId(tenantId);
						if (StringUtils.isEmpty(mifr.getId()))
							mifr.setId(materialIssuedFromReceiptsJdbcRepository
									.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(), 1).get(0));
						materialIssuedFromReceiptsIds.add(mifr.getId());
					}
					materialIssuedFromReceiptsJdbcRepository.markDeleted(materialIssuedFromReceiptsIds, tenantId,
							"materialissuedfromreceipt", "issuedetailid", materialIssueDetail.getId());
				}
				materialIssueDetailsJdbcRepository.markDeleted(materialIssueDetailsIds, tenantId, "materialissuedetail",
						"materialissuenumber", materialIssue.getIssueNumber());

			}
			kafkaTemplate.send(updateTopic, updateKey, materialIssueRequest);
			MaterialIssueResponse response = new MaterialIssueResponse();
			response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
			response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
			return response;
		}

		public MaterialIssueResponse search(MaterialIssueSearchContract searchContract) {
			Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract,IssueTypeEnum.NONINDENTISSUE.toString());
			if (materialIssues.getPagedData().size() > 0)
				for (MaterialIssue materialIssue : materialIssues.getPagedData()) {
					Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository
							.search(materialIssue.getIssueNumber(), materialIssue.getTenantId(),IssueTypeEnum.NONINDENTISSUE.toString());
					if (materialIssueDetails.getPagedData().size() > 0) {
						for (MaterialIssueDetail materialIssueDetail : materialIssueDetails.getPagedData()) {
							Uom uom = null;
							if (materialIssueDetail.getUom() != null && materialIssueDetail.getUom().getCode() != null)
								uom = getUom(materialIssueDetail.getTenantId(), materialIssueDetail.getUom().getCode(),
										new RequestInfo());
							Double quantityIssued = getSearchConvertedQuantity(
									Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
									Double.valueOf(uom.getConversionFactor().toString()));
							materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
						}
						materialIssue.setMaterialIssueDetails(materialIssueDetails.getPagedData());
					}
				}

			MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse();
			materialIssueResponse.setMaterialIssues(materialIssues.getPagedData());
			return materialIssueResponse;
		}



}
