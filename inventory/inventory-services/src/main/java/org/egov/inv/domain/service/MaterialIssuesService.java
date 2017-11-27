package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.Constants;
import org.egov.common.DomainService;
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
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.MaterialIssueDetailsJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptsJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MaterialIssuesService extends DomainService {

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

	public MaterialIssueResponse create(final MaterialIssueRequest materialIssueRequest) {
		try {
			validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					materialIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				materialIssue.setIssueNumber("MRIN-" + String.valueOf(year) + "-" + seqNo);
				materialIssue.setAuditDetails(mapAuditDetails(materialIssueRequest.getRequestInfo()));
				i++;
				int j = 0;
				List<String> detailSequenceNos = materialIssueJdbcRepository.getSequence(
						MaterialIssueDetail.class.getSimpleName(), materialIssue.getMaterialIssueDetails().size());
				for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
					materialIssueDetail.setId(detailSequenceNos.get(j));
					materialIssueDetail.setTenantId(materialIssue.getTenantId());
					j++;

					int k = 0;
					List<String> materialIssuedFromReceiptsSeqNos = materialIssueJdbcRepository.getSequence(
							MaterialIssuedFromReceipt.class.getSimpleName(),
							materialIssueDetail.getMaterialIssuedFromReceipts().size());
					for (MaterialIssuedFromReceipt materialIssuedFromReceipts : materialIssueDetail
							.getMaterialIssuedFromReceipts()) {
						materialIssuedFromReceipts.setId(materialIssuedFromReceiptsSeqNos.get(k));
						materialIssuedFromReceipts.setTenantId(materialIssueDetail.getTenantId());
						k++;
					}
				}
			}
			kafkaTemplate.send(createTopic, createKey, materialIssueRequest);
			MaterialIssueResponse response = new MaterialIssueResponse();
			response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
			response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
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
							new MaterialIssueEntity().toEntity(materialIssue))) {

					}
				}
				break;
			case "update":
				for (MaterialIssue materialIssue : materialIssues) {
				if(StringUtils.isEmpty(materialIssue.getIssueNumber()))
						throw new CustomBindException("Issue Number is not provided");
				}
			default:

			}
		} catch (IllegalArgumentException e) {
		}

	}

	public MaterialIssueResponse update(final MaterialIssueRequest materialIssueRequest, String tenantId) {
		validate(materialIssueRequest.getMaterialIssues(),Constants.ACTION_UPDATE);
		List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
		for(MaterialIssue materialIssue:materialIssues){
			List<String> materialIssueDetailsIds = new ArrayList<>();
			if(StringUtils.isEmpty(materialIssue.getTenantId()))
				materialIssue.setTenantId(tenantId);
			materialIssue.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
		for(MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()){
			List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
			if(StringUtils.isEmpty(materialIssueDetail.getTenantId()))
				materialIssueDetail.setTenantId(tenantId);
			if(StringUtils.isEmpty(materialIssueDetail.getId()))		
				materialIssueDetail.setId(materialIssueDetailsJdbcRepository.getSequence(MaterialIssueDetail.class.getSimpleName(),1).get(0));
			materialIssueDetailsIds.add(materialIssueDetail.getId());
			for(MaterialIssuedFromReceipt mifr: materialIssueDetail.getMaterialIssuedFromReceipts())
			{
				if(StringUtils.isEmpty(mifr.getTenantId()))
					mifr.setTenantId(tenantId);
				if(StringUtils.isEmpty(mifr.getId()))
					mifr.setId(materialIssuedFromReceiptsJdbcRepository.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(),1).get(0));
				materialIssuedFromReceiptsIds.add(mifr.getId());
			}
			materialIssuedFromReceiptsJdbcRepository.markDeleted(materialIssuedFromReceiptsIds, tenantId, "materialissuedfromreceipt", "issuedetailid", materialIssueDetail.getId());
		}
		materialIssueDetailsJdbcRepository.markDeleted(materialIssueDetailsIds, tenantId, "materialissuedetail", "materialissuenumber", materialIssue.getIssueNumber());

		}
		kafkaTemplate.send(updateTopic, updateKey, materialIssueRequest);
		MaterialIssueResponse response = new MaterialIssueResponse();
		response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
		response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
		return response;
	}

	public MaterialIssueResponse search(final MaterialIssueSearchContract searchContract) {
		Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract);
		if(materialIssues.getPagedData().size() >0)
		for(MaterialIssue materialIssue : materialIssues.getPagedData())
		{
			Pagination<MaterialIssueDetail> materialIssueDetails =	materialIssueDetailsJdbcRepository.search(materialIssue.getIssueNumber(), materialIssue.getTenantId());
			materialIssue.setMaterialIssueDetails(materialIssueDetails.getPagedData());
		}
		MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse();
		materialIssueResponse.setMaterialIssues(materialIssues.getPagedData());
		return materialIssueResponse;

	}

}
