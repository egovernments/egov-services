package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Scrap;
import org.egov.inv.model.ScrapDetail;
import org.egov.inv.model.ScrapDetailSearch;
import org.egov.inv.model.ScrapRequest;
import org.egov.inv.model.ScrapResponse;
import org.egov.inv.model.ScrapSearch;
import org.egov.inv.persistence.repository.ScrapDetailJdbcRepository;
import org.egov.inv.persistence.repository.ScrapJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ScrapService extends DomainService{
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private NonIndentMaterialIssueService nonIndentMaterialIssueService;
	
	@Autowired
	private ScrapJdbcRepository scrapJdbcRepository;
	
	@Autowired
	private ScrapDetailJdbcRepository scrapDetailJdbcRepository;
	
	@Value("${inv.scrap.save.topic}")
	private String createTopic;
	
	@Value("${inv.scrap.update.topic}")
	private String updateTopic;
	
	public List<Scrap> create(ScrapRequest scrapReq, String tenantId) {
	try{
		fetchRelated(scrapReq,tenantId);
		List<Scrap> scrap = scrapReq.getScraps();
		validate(scrapReq.getScraps(), Constants.ACTION_CREATE,tenantId,scrapReq.getRequestInfo());
		
		scrap.forEach(scrapData -> {
			scrapData.setId(scrapJdbcRepository.getSequence("seq_scrap"));
			scrapData.setScrapNumber(appendString(scrapData));
			if (StringUtils.isEmpty(scrapData.getTenantId())) {
				scrapData.setTenantId(tenantId);
			}
			
			scrapData.getScrapDetails().forEach(scrapDetails -> {
				scrapDetails.setId(scrapJdbcRepository.getSequence("seq_scrapDetail"));
				scrapDetails.setTenantId(tenantId);
			});
		});
		
		kafkaTemplate.send(createTopic, scrapReq);
		return scrapReq.getScraps();
	}catch(CustomBindException e){
		throw e;
	}
	}
	
	public List<Scrap> update(ScrapRequest scrapReq, String tenantId) {
		try{
			List<Scrap> scrap = scrapReq.getScraps();
			validate(scrapReq.getScraps(), Constants.ACTION_UPDATE,tenantId,scrapReq.getRequestInfo());
			
			scrap.forEach(scrapData -> {
				if (StringUtils.isEmpty(scrapData.getTenantId())) {
					scrapData.setTenantId(tenantId);
				}
				
				scrapData.getScrapDetails().forEach(scrapDetails -> {
					if (StringUtils.isEmpty(scrapDetails.getTenantId())) {
						scrapDetails.setTenantId(tenantId);
					}
				});
			});
			kafkaTemplate.send(updateTopic, scrapReq);
			return scrapReq.getScraps();
		}catch(CustomBindException e){
			throw e;
		}
		}
	
	public ScrapResponse search(ScrapSearch scrapSearch) {
		Pagination<Scrap> scrapPagination = scrapJdbcRepository.search(scrapSearch);
		if (scrapPagination.getPagedData().size() > 0) {
			
	            for (Scrap scrap : scrapPagination.getPagedData()) {
	                List<ScrapDetail> scrapDetail = getScrapDetail(scrap.getScrapNumber(), scrapSearch.getTenantId());
	                scrap.setScrapDetails(scrapDetail);
	            }
	        
		}
		ScrapResponse response = new ScrapResponse();
		return response.responseInfo(null).scraps(scrapPagination.getPagedData().size() > 0
				? scrapPagination.getPagedData() : Collections.EMPTY_LIST);
	}
	
	private void validate(List<Scrap> receipt, String method, String tenantId,RequestInfo info) {
		InvalidDataException errors = new InvalidDataException();

		try {
			switch (method) {
				case Constants.ACTION_CREATE: {
					if (receipt == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"scrap", null);
					} 
				}
					break;
	
				case Constants.ACTION_UPDATE: {
					if (receipt == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"scrap", null);
					}
				}
					break;
	
				}
		}catch(IllegalArgumentException e){
			throw e;
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}
	
	private String appendString(Scrap scrapReq) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String code = "SCRP/";
		int id = Integer.valueOf(scrapJdbcRepository.getSequence(scrapReq));
		String idgen = String.format("%05d", id);
		String scrapNumber = code + idgen + "/" + year;
		return scrapNumber;
	}
	
	private void fetchRelated(ScrapRequest request, String tenantId) {
		InvalidDataException errors = new InvalidDataException();
		List<ScrapDetail> scrapDetailList = new ArrayList<>();
		for (Scrap scrap : request.getScraps()) {
		MaterialIssueSearchContract searchContract = MaterialIssueSearchContract.builder()
													.issuePurpose(MaterialIssue.IssuePurposeEnum.WRITEOFFORSCRAP.toString())
													.tenantId(tenantId)
													.build();
													
		MaterialIssueResponse response = nonIndentMaterialIssueService.search(searchContract);
		for(MaterialIssue issue : response.getMaterialIssues()){
			for(MaterialIssueDetail detail : issue.getMaterialIssueDetails())
			{
				ScrapDetail scrapDetail= new ScrapDetail();
		if (issue == null )
			errors.addDataError(ErrorCode.DOESNT_MATCH.getCode(), "issuePurpose", null);
		
		else
			scrapDetail.setTenantId(tenantId); 
			scrapDetail.setUom(detail.getUom());
			scrapDetail.setMaterial(detail.getMaterial());
			scrapDetail.setExistingValue(detail.getValue());
			scrapDetailList.add(scrapDetail);
			}
		}
		scrap.setScrapDetails(scrapDetailList);
		if (errors.getValidationErrors().size() > 0)
			throw errors;
				}
		}
	
	 private List<ScrapDetail> getScrapDetail(String scrapNumber, String tenantId) {
	        ScrapDetailSearch scrapDetailSearch = ScrapDetailSearch.builder()
	                .ScrapNumber(scrapNumber)
	                .tenantId(tenantId)
	                .build();
	        Pagination<ScrapDetail> scrapDetails = scrapDetailJdbcRepository.search(scrapDetailSearch);
	        return scrapDetails.getPagedData().size() > 0 ? scrapDetails.getPagedData() : Collections.EMPTY_LIST;
	    }
}


