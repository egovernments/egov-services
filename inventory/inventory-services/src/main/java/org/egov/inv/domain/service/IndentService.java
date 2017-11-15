package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Indent;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentRequest;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IndentService extends DomainService {
	/*
	 * @Autowired private StoreRepository storeRepository;
	 * 
	 * @Autowired private DepartmentRepository departmentRepository;
	 */

	 
	@Autowired
	private IndentJdbcRepository indentRepository;
	@Value("${inv.indents.save.topic}")
	private String saveTopic;
	@Value("${inv.indents.save.key}")
	private String saveKey;

	@Value("${inv.indents.save.topic}")
	private String updateTopic;
	@Value("${inv.indents.save.key}")
	private String updateKey;

	@Transactional
	public IndentResponse create(IndentRequest indentRequest) {

		try {
			List<Indent> indents = fetchRelated(indentRequest.getIndents());
			validate(indents, Constants.ACTION_CREATE);
		    List<String> sequenceNos = indentRepository.getSequence(Indent.class.getSimpleName(),indents.size());
		    int i=0;
			for (Indent b : indents) {
				b.setId(sequenceNos.get(i));
				//move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
				b.setIndentNumber(sequenceNos.get(i));
			    i++;
			    int j=0;
			    b.setAuditDetails(getAuditDetails(indentRequest.getRequestInfo(), Constants.ACTION_CREATE));
			    List<String> detailSequenceNos = indentRepository.getSequence(IndentDetail.class.getSimpleName(),indents.size()); 
			    for(IndentDetail d : b.getIndentDetails())
			    {
			    	 d.setId(detailSequenceNos.get(j));
			    	 d.setTenantId(b.getTenantId());
			    	 j++;
			    }
			}
			kafkaQue.send(saveTopic, saveKey, indentRequest);
			
			IndentResponse response = new IndentResponse();
			response.setIndents(indentRequest.getIndents());
			response.setResponseInfo(getResponseInfo(indentRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}
	@Transactional
	public IndentResponse update(IndentRequest indentRequest) {

		try {
			List<Indent> indents = fetchRelated(indentRequest.getIndents());
			validate(indents, Constants.ACTION_UPDATE);
		   
		    
			 
			kafkaQue.send(saveTopic, saveKey, indentRequest);
			
			IndentResponse response = new IndentResponse();
			response.setIndents(indentRequest.getIndents());
			response.setResponseInfo(getResponseInfo(indentRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}
	
	 
	public IndentResponse search(IndentSearch is) {
		IndentResponse response=new IndentResponse();
		 Pagination<Indent> search = indentRepository.search(is);
		 response.setIndents(search.getPagedData());
		 response.setPage(getPage(search));
		 return response;
	
	}
	private void validate(List<Indent> indents, String method) {

		try {
			switch (method) {

			case Constants.ACTION_CREATE: {
				if (indents == null) {
					throw new InvalidDataException("indents", ErrorCode.NOT_NULL.getCode(), null);
				}
			}
				break;

			}
		} catch (IllegalArgumentException e) {

		}

	}

	public List<Indent> fetchRelated(List<Indent> indents) {
		for (Indent indent : indents) {
			// fetch related items
			/*
			 * if (indent.getIssueStore() != null) { Store issueStore =
			 * storeRepository.findById(indent.getIssueStore()); if (issueStore
			 * == null) { throw new InvalidDataException("issueStore",
			 * "issueStore.invalid", " Invalid issueStore"); }
			 * indent.setIssueStore(issueStore); } if (indent.getIndentStore()
			 * != null) { Store indentStore =
			 * storeRepository.findById(indent.getIndentStore()); if
			 * (indentStore == null) { throw new
			 * InvalidDataException("indentStore", "indentStore.invalid",
			 * " Invalid indentStore"); } indent.setIndentStore(indentStore); }
			 * if (indent.getDepartment() != null) { Department department =
			 * departmentRepository.findById(indent.getDepartment()); if
			 * (department == null) { throw new
			 * InvalidDataException("department", "department.invalid",
			 * " Invalid department"); } indent.setDepartment(department); }
			 */

		}

		return indents;
	}

}