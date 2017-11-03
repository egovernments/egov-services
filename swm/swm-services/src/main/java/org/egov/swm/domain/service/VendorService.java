package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.VendorRepository;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@Transactional(readOnly = true)
public class VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Value("${egov.swm.vendor.num.idgen.name}")
	private String idGenNameForVendorNumPath;

	@Value("${egov.swm.contractor.num.idgen.name}")
	private String idGenNameForContractorNumPath;

	@Transactional
	public VendorRequest create(VendorRequest vendorRequest) {

		Long userId = null;

		for (Vendor v : vendorRequest.getVendors()) {

			if (vendorRequest.getRequestInfo() != null && vendorRequest.getRequestInfo().getUserInfo() != null
					&& null != vendorRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vendorRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			v.setVendorNo(generateVendorNumber(v.getTenantId(), vendorRequest.getRequestInfo()));

			if (v.getContractor() != null) {
				v.getContractor().setTenantId(v.getTenantId());
				v.getContractor().setContractorNo(
						generateContractorNumber(v.getContractor().getTenantId(), vendorRequest.getRequestInfo()));
				v.getContractor().setAuditDetails(v.getAuditDetails());
			}

			prepareAgreementDocument(v);

		}

		return vendorRepository.save(vendorRequest);

	}

	@Transactional
	public VendorRequest update(VendorRequest vendorRequest) {

		Long userId = null;

		for (Vendor v : vendorRequest.getVendors()) {

			if (vendorRequest.getRequestInfo() != null && vendorRequest.getRequestInfo().getUserInfo() != null
					&& null != vendorRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vendorRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			VendorSearch vendorSearch = new VendorSearch();
			vendorSearch.setTenantId(v.getTenantId());
			vendorSearch.setVendorNo(v.getVendorNo());
			Pagination<Vendor> vendorSearchResult = search(vendorSearch);

			if (vendorSearchResult != null && vendorSearchResult.getPagedData() != null
					&& !vendorSearchResult.getPagedData().isEmpty()) {
				v.getContractor().setTenantId(v.getTenantId());
				v.getContractor().setContractorNo(vendorSearchResult.getPagedData().get(0).getContractor().getContractorNo());
			}

		}

		return vendorRepository.update(vendorRequest);

	}

	private void prepareAgreementDocument(Vendor v) {

		if (v.getAgreementDocument() != null && v.getAgreementDocument().getFileStoreId() != null) {
			v.getAgreementDocument().setId(UUID.randomUUID().toString().replace("-", ""));
			v.getAgreementDocument().setTenantId(v.getTenantId());
			v.getAgreementDocument().setRefCode(v.getVendorNo());
			v.getAgreementDocument().setAuditDetails(v.getAuditDetails());
		}
	}

	private String generateVendorNumber(String tenantId, RequestInfo requestInfo) {

		String vendorNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVendorNumPath);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					vendorNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return vendorNumber;
	}

	private String generateContractorNumber(String tenantId, RequestInfo requestInfo) {

		String contractorNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForContractorNumPath);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					contractorNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return contractorNumber;
	}

	public Pagination<Vendor> search(VendorSearch vendorSearch) {

		return vendorRepository.search(vendorSearch);
	}

	private void setAuditDetails(Vendor contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getVendorNo() || contract.getVendorNo().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}