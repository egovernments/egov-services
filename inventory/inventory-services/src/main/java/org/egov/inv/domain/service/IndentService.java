package org.egov.inv.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Indent;
import org.egov.inv.model.Indent.IndentPurposeEnum;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentRequest;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.persistence.entity.IndentDetailEntity;
import org.egov.inv.persistence.repository.IndentDetailJdbcRepository;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.egov.inv.model.Error;

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

	@Value("${inv.indents.update.topic}")
	private String updateTopic;
	@Value("${inv.indents.update.key}")
	private String updateKey;

	@Autowired
	private IndentDetailJdbcRepository indentDetailJdbcRepository;

	private static final Logger LOG = LoggerFactory.getLogger(IndentService.class);

	@Transactional
	public IndentResponse create(IndentRequest indentRequest) {

		try {
			List<Indent> indents = fetchRelated(indentRequest.getIndents());
			validate(indents, Constants.ACTION_CREATE);
			List<String> sequenceNos = indentRepository.getSequence(Indent.class.getSimpleName(), indents.size());
			int i = 0;
			for (Indent b : indents) {
				b.setId(sequenceNos.get(i));
				// move to id-gen with format <ULB short code>/<Store
				// Code>/<fin. Year>/<serial No.>
				b.setIndentNumber(sequenceNos.get(i));
				i++;
				int j = 0;
				//TO-DO : when workflow implemented change this to created
				b.setIndentStatus(IndentStatusEnum.APPROVED);
				b.setAuditDetails(getAuditDetails(indentRequest.getRequestInfo(), Constants.ACTION_CREATE));
				List<String> detailSequenceNos = indentRepository.getSequence(IndentDetail.class.getSimpleName(),
						b.getIndentDetails().size());
				for (IndentDetail d : b.getIndentDetails()) {
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
			String tenantId="";
			List<Indent> indents = fetchRelated(indentRequest.getIndents());
			String indentNumber="";
			List<String> ids=new ArrayList<String>();
			validate(indents, Constants.ACTION_UPDATE);
			for (Indent b : indents) {
				int j=0;
				if(!indentNumber.isEmpty())
					indentNumber=b.getIndentNumber();
				b.setAuditDetails(getAuditDetails(indentRequest.getRequestInfo(), Constants.ACTION_UPDATE));
				for (IndentDetail d : b.getIndentDetails()) {
					if(d.getId()==null)
						d.setId(indentRepository.getSequence(IndentDetail.class.getSimpleName(),1).get(0));
					ids.add(d.getId());
					d.setTenantId(b.getTenantId());
					if(tenantId.isEmpty())
						tenantId=b.getTenantId();
					j++;
				}
			}

			kafkaQue.send(saveTopic, saveKey, indentRequest);
			indentDetailJdbcRepository.markDeleted(ids,tenantId,"indentdetail","indentNumber",indentNumber);
			IndentResponse response = new IndentResponse();
			response.setIndents(indentRequest.getIndents());
			response.setResponseInfo(getResponseInfo(indentRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}

	public IndentResponse search(IndentSearch is) {
		IndentResponse response = new IndentResponse();
		Pagination<Indent> search = indentRepository.search(is);
		if (!search.getPagedData().isEmpty()) {
			List<String> indentNumbers = new ArrayList<>();
			for (Indent indent : search.getPagedData()) {
				indentNumbers.add(indent.getIndentNumber());
			}

			List<IndentDetailEntity> indentDetails = indentDetailJdbcRepository.find(indentNumbers, is.getTenantId());

			IndentDetail detail = null;
			for (Indent indent : search.getPagedData()) {
				for (IndentDetailEntity detailEntity : indentDetails) {
					if (indent.getIndentNumber().equalsIgnoreCase(detailEntity.getIndentNumber())) {
						detail = detailEntity.toDomain();
						indent.addIndentDetailsItem(detail);
					}
				}
			}
		}
		response.setIndents(search.getPagedData());
		response.setPage(getPage(search));
		return response;

	}

	private void validate(List<Indent> indents, String method) {
		InvalidDataException	errors=	  new InvalidDataException();
		try {
			Long currentDate= currentEpochWithoutTime();
			currentDate=currentDate+(24*60*60)-1; 
			LOG.info("CurrentDate is "+			toDateStr(currentDate));

			Long ll=new Date().getTime();
			switch (method) {

			case Constants.ACTION_CREATE: {
				if (indents == null) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(),"indents","null");  
				}
				for(Indent indent: indents)
				{
					LOG.info("indentDate is "+			toDateStr(indent.getIndentDate()));
					LOG.info("compare  "+indent.getIndentDate().compareTo(currentDate));
					LOG.info("compare  "+ll.compareTo(currentDate));

					if(indent.getIndentDate().compareTo(currentDate) >= 0)
					{
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "indentDate",indent.getIndentDate().toString());	
					}
                   // commeneted as of now to support the past dated entries
					/*if(indent.getExpectedDeliveryDate().compareTo(currentDate) < 0)
					{
						errors.addDataError(ErrorCode.DATE_GE_CURRENTDATE.getCode(), "expectedDeliveryDate",indent.getExpectedDeliveryDate().toString());		
					}*/
					if(indent.getIndentDate().compareTo(indent.getExpectedDeliveryDate())>0)
					{
						LOG.info("expectedDeliveryDate="+toDateStr(indent.getExpectedDeliveryDate()));
						LOG.info("indentDate="+toDateStr(indent.getIndentDate()));
						errors.addDataError( ErrorCode.DATE1_GE_DATE2.getCode(), "expectedDeliveryDate", "indentDate",
								indent.getExpectedDeliveryDate().toString(),indent.getIndentDate().toString());	
					}
					List<String> materialCodes=new ArrayList<>();
					int i=0;
					for(IndentDetail detail:indent.getIndentDetails())
					{
						++i;
						if(materialCodes.isEmpty())
						{

							materialCodes.add(detail.getMaterial().getCode());

						}else
						{	
							if(materialCodes.indexOf(detail.getMaterial().getCode())==-1)
							{
								materialCodes.add(detail.getMaterial().getCode());
							}

							else
							{
								errors.addDataError(ErrorCode.REPEATED_VALUE.getCode(), "material",detail.getMaterial().getCode(),
										" at serial no. "+i+" and "+ (materialCodes.indexOf(detail.getMaterial().getCode())+1));
							}
						}

						if(indent.getIndentPurpose().equals(IndentPurposeEnum.CAPITAL))
						{
							if(detail.getProjectCode()==null || detail.getProjectCode().getCode()==null)
								errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "projectCode" ,"indentPurpose=Capital","at serail no. "+i);
						}
						if(indent.getIndentPurpose().equals(IndentPurposeEnum.REPAIRSANDMAINTENANCE))
						{
							if(detail.getAsset()==null || detail.getAsset().getCode()==null)
								errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "assetCode" ,
										"indentPurpose=Repairs and Maintenance" ,"at seraill no. "+i);
						}

					}
				}

			}
			break;

			}
		} catch (IllegalArgumentException e) {

		}
		if(errors.getValidationErrors().size()>0)
			throw errors;

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