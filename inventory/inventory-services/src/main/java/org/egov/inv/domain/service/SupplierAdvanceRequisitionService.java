package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.AdvanceRequisition;
import org.egov.inv.model.AdvanceRequisitionDetails;
import org.egov.inv.model.AdvanceRequisitionStatus;
import org.egov.inv.model.AdvanceRequisitionType;
import org.egov.inv.model.ChartOfAccount;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.SupplierAdvanceRequisition;
import org.egov.inv.model.SupplierAdvanceRequisitionRequest;
import org.egov.inv.model.SupplierAdvanceRequisitionResponse;
import org.egov.inv.model.SupplierAdvanceRequisitionSearch;
import org.egov.inv.persistence.repository.SupplierAdvanceRequisitionJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class SupplierAdvanceRequisitionService extends DomainService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SupplierAdvanceRequisitionJdbcRepository supplierAdvanceRequisitionRepository;

	@Value("${inv.supplieradvancerequisition.save.topic}")
	private String saveTopic;

	@Value("${inv.supplieradvancerequisition.save.key}")
	private String saveKey;

	@Value("${inv.supplieradvancerequisition.update.topic}")
	private String updateTopic;

	@Value("${inv.supplieradvancerequisition.update.key}")
	private String updateKey;

	@Transactional
	public SupplierAdvanceRequisitionResponse create(SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest) {

		try {
			validate(supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions(), Constants.ACTION_CREATE);
			List<String> sequenceNos = supplierAdvanceRequisitionRepository.getSequence(SupplierAdvanceRequisition.class.getSimpleName(),
					supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions().size());
			int i = 0;
			for (SupplierAdvanceRequisition b : supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions()) {
				ArrayList<AdvanceRequisitionDetails> lard = new ArrayList<AdvanceRequisitionDetails>();
				//since there will be no debit in case of advance amount, only credit amount populated in details.
				lard.add(AdvanceRequisitionDetails.builder().creditAmount(b.getAdvanceAdjustedAmount()).chartOfAccounts(ChartOfAccount.builder().name(AdvanceRequisitionType.SUPPLIER.name()).tenantId(b.getTenantId()).build()).build());
				AdvanceRequisition advReq = AdvanceRequisition.builder().arfType(AdvanceRequisitionType.SUPPLIER).advanceRequisitionDate(new Date().getTime()).status(AdvanceRequisitionStatus.APPROVED).amount(b.getAdvanceAdjustedAmount()).tenantId(b.getTenantId()).advanceRequisitionDetails(lard).build();
				//persist the above advance requisition by sending the object to financial service, get the id, set that id as id for supplier advance requisition
				b.setId(sequenceNos.get(i));
				i++;
				b.setAuditDetails(getAuditDetails(supplierAdvanceRequisitionRequest.getRequestInfo(), Constants.ACTION_CREATE));
			}
			kafkaQue.send(saveTopic, saveKey, supplierAdvanceRequisitionRequest);
			SupplierAdvanceRequisitionResponse response = new SupplierAdvanceRequisitionResponse();
			response.setSupplierAdvanceRequisitions(supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions());
			response.setResponseInfo(getResponseInfo(supplierAdvanceRequisitionRequest.getRequestInfo()));
			return response;
		} catch (Exception e) {
			throw e;
		}

	}

	@Transactional
	public SupplierAdvanceRequisitionResponse update(SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest) {

		try {
			validate(supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions(), Constants.ACTION_UPDATE);
			for (SupplierAdvanceRequisition b : supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions()) {
				b.setAuditDetails(getAuditDetails(supplierAdvanceRequisitionRequest.getRequestInfo(), Constants.ACTION_UPDATE));
			}

			kafkaQue.send(updateTopic, updateKey, supplierAdvanceRequisitionRequest);
			SupplierAdvanceRequisitionResponse response = new SupplierAdvanceRequisitionResponse();
			response.setSupplierAdvanceRequisitions(supplierAdvanceRequisitionRequest.getSupplierAdvanceRequisitions());
			response.setResponseInfo(getResponseInfo(supplierAdvanceRequisitionRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}

	public SupplierAdvanceRequisitionResponse search(SupplierAdvanceRequisitionSearch is, RequestInfo info) {

		SupplierAdvanceRequisitionResponse response = new SupplierAdvanceRequisitionResponse();
		Pagination<SupplierAdvanceRequisition> search = supplierAdvanceRequisitionRepository.search(is);
		if (!search.getPagedData().isEmpty()) {
			List<String> supplierAdvanceRequisitionNumbers = new ArrayList<>();
			for (SupplierAdvanceRequisition supplierAdvanceRequisition : search.getPagedData()) {
				supplierAdvanceRequisitionNumbers.add(supplierAdvanceRequisition.getId());
			}
		}
		response.setSupplierAdvanceRequisitions(search.getPagedData());
		response.setPage(getPage(search));
		return response;

	}

	private void validate(List<SupplierAdvanceRequisition> supplierAdvanceRequisitions, String method) {
		InvalidDataException errors = new InvalidDataException();
		try {

			switch (method) {
				case Constants.ACTION_UPDATE: {
					if (supplierAdvanceRequisitions == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(), "supplierAdvanceRequisitions", "null");
					}
					if(errors.getValidationErrors().size()>0)
						break;
				}
	
				case Constants.ACTION_CREATE: {
					if (supplierAdvanceRequisitions == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(), "supplierAdvanceRequisitions", "null");
					}
					if(errors.getValidationErrors().size()>0)
						break;
				}
			}
			
			for(SupplierAdvanceRequisition supplierAdvanceRequisition : supplierAdvanceRequisitions) {
				if(!supplierAdvanceRequisitionRepository.checkPOValidity(supplierAdvanceRequisition.getPurchaseOrder().getPurchaseOrderNumber(), supplierAdvanceRequisition.getTenantId())) {
					errors.addDataError(ErrorCode.INVALID_PONUMBER_FOR_ADVREQ.getCode(), "purchaseOrderNumber", supplierAdvanceRequisition.getPurchaseOrder().getPurchaseOrderNumber());
				}
			}
			
		} catch (IllegalArgumentException e) {

		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;

	}
	
}