package org.egov.inv.domain.service;

import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Indent;
import org.egov.inv.model.IndentRequest;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IndentService  extends DomainService{
 	/*@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
 */
	
	@Autowired
	protected LogAwareKafkaTemplate<String, Object> kafkaQue;
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
	public IndentRequest create(IndentRequest indentRequest  ) {

		try {
			List<Indent> indents = fetchRelated(indentRequest.getIndents());
			validate(indents, Constants.ACTION_CREATE);
			for (Indent b : indents) {
				b.setId(indentRepository.getSequence(b));
				//b.add();
			}
			kafkaQue.send(saveTopic, saveKey, indentRequest);
		} catch (CustomBindException e) {
			throw e;
		}

		return indentRequest;

	}

	 

	private void validate(List<Indent> indents, String method ) {

		try {
			switch (method) {
			 
			case Constants.ACTION_CREATE:{
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
		/*	if (indent.getIssueStore() != null) {
				Store issueStore = storeRepository.findById(indent.getIssueStore());
				if (issueStore == null) {
					throw new InvalidDataException("issueStore", "issueStore.invalid", " Invalid issueStore");
				}
				indent.setIssueStore(issueStore);
			}
			if (indent.getIndentStore() != null) {
				Store indentStore = storeRepository.findById(indent.getIndentStore());
				if (indentStore == null) {
					throw new InvalidDataException("indentStore", "indentStore.invalid", " Invalid indentStore");
				}
				indent.setIndentStore(indentStore);
			}
			if (indent.getDepartment() != null) {
				Department department = departmentRepository.findById(indent.getDepartment());
				if (department == null) {
					throw new InvalidDataException("department", "department.invalid", " Invalid department");
				}
				indent.setDepartment(department);
			}*/
			 

		 

		}

		return indents;
	}


 

	 

}