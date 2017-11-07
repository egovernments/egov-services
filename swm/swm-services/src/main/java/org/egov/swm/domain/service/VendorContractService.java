package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.VendorContractRepository;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorContractRequest;
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
public class VendorContractService {

	@Autowired
	private VendorContractRepository vendorContractRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Autowired
	private VendorService vendorService;

	@Value("${egov.swm.vendor.contract.num.idgen.name}")
	private String idGenNameForVendorContractNumPath;

	@Transactional
	public VendorContractRequest create(VendorContractRequest vendorContractRequest) {

		validate(vendorContractRequest);

		Long userId = null;
		if (vendorContractRequest.getVendorContracts() != null)
			for (VendorContract vc : vendorContractRequest.getVendorContracts()) {

				if (vendorContractRequest.getRequestInfo() != null
						&& vendorContractRequest.getRequestInfo().getUserInfo() != null
						&& null != vendorContractRequest.getRequestInfo().getUserInfo().getId()) {
					userId = vendorContractRequest.getRequestInfo().getUserInfo().getId();
				}

				setAuditDetails(vc, userId);

				vc.setContractNo(
						generateVendorContractNumber(vc.getTenantId(), vendorContractRequest.getRequestInfo()));

			}

		return vendorContractRepository.save(vendorContractRequest);

	}

	@Transactional
	public VendorContractRequest update(VendorContractRequest vendorContractRequest) {

		validate(vendorContractRequest);

		Long userId = null;

		if (vendorContractRequest.getVendorContracts() != null)
			for (VendorContract vc : vendorContractRequest.getVendorContracts()) {

				if (vendorContractRequest.getRequestInfo() != null
						&& vendorContractRequest.getRequestInfo().getUserInfo() != null
						&& null != vendorContractRequest.getRequestInfo().getUserInfo().getId()) {
					userId = vendorContractRequest.getRequestInfo().getUserInfo().getId();
				}

				setAuditDetails(vc, userId);

			}

		return vendorContractRepository.update(vendorContractRequest);

	}

	private String generateVendorContractNumber(String tenantId, RequestInfo requestInfo) {

		String vendorContractNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVendorContractNumPath);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					vendorContractNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return vendorContractNumber;
	}

	private void validate(VendorContractRequest vendorContractRequest) {

		VendorSearch vendorSearch;
		Pagination<Vendor> vendors;
		for (VendorContract vendorContract : vendorContractRequest.getVendorContracts()) {

			if (vendorContract.getVendor() != null && vendorContract.getVendor().getVendorNo() != null) {
				vendorSearch = new VendorSearch();
				vendorSearch.setTenantId(vendorContract.getTenantId());
				vendorSearch.setVendorNo(vendorContract.getVendor().getVendorNo());
				vendors = vendorService.search(vendorSearch);
				if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty()) {
					vendorContract.setVendor(vendors.getPagedData().get(0));
				} else {
					throw new CustomException("Vendor",
							"Given Vendor is invalid: " + vendorContract.getVendor().getVendorNo());
				}
			}

			if (vendorContract.getContractPeriodFrom() != null && vendorContract.getContractPeriodTo() != null) {

				if (new Date(vendorContract.getContractPeriodTo())
						.before(new Date(vendorContract.getContractPeriodFrom()))) {
					throw new CustomException("ContractPeriodToDate ", "Given Contract Period To Date is invalid: "
							+ new Date(vendorContract.getContractPeriodTo()));
				}
			}

		}

	}

	public Pagination<VendorContract> search(VendorContractSearch vendorContractSearch) {

		return vendorContractRepository.search(vendorContractSearch);
	}

	private void setAuditDetails(VendorContract vc, Long userId) {

		if (vc.getAuditDetails() == null)
			vc.setAuditDetails(new AuditDetails());

		if (null == vc.getContractNo() || vc.getContractNo().isEmpty()) {
			vc.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			vc.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		vc.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		vc.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}