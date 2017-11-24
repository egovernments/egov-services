package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.JdbcRepository;
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
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.MaterialIssueDetailsJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
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

	@Value("${inv.issues.save.topic}")
	private String createTopic;

	@Value("${inv.issues.save.key}")
	private String createKey;

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
			default:

			}
		} catch (IllegalArgumentException e) {
		}

	}

	public void update(final MaterialIssueRequest materialIssueRequest) {
		
		
		List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
		List<String> materialIssueNumbers = materialIssues.stream().map(materialIssue -> materialIssue.getIssueNumber())
				.collect(Collectors.toList());
		List<MaterialIssueDetail> matyerialIssueDetails = materialIssueJdbcRepository.getMaterialIssueDetailsByIssueNumber(materialIssueRequest.getMaterialIssues().get(0).getTenantId(),
				materialIssueNumbers);
		materialIssueJdbcRepository.compareAndGenerateObjects(materialIssueRequest.getMaterialIssues(),
				matyerialIssueDetails);
		
		

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
