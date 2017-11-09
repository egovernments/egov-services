package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.SupplierRepository;
import org.egov.swm.domain.repository.VendorRepository;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Value("${egov.swm.vendor.num.idgen.name}")
	private String idGenNameForVendorNumPath;

	@Value("${egov.swm.supplier.num.idgen.name}")
	private String idGenNameForSupplierNumPath;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Transactional
	public VendorRequest create(VendorRequest vendorRequest) {

		validate(vendorRequest);

		Long userId = null;

		for (Vendor v : vendorRequest.getVendors()) {

			if (vendorRequest.getRequestInfo() != null && vendorRequest.getRequestInfo().getUserInfo() != null
					&& null != vendorRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vendorRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			v.setVendorNo(generateVendorNumber(v.getTenantId(), vendorRequest.getRequestInfo()));

			if (v.getSupplier() != null) {
				v.getSupplier().setTenantId(v.getTenantId());
				v.getSupplier().setSupplierNo(
						generateSupplierNumber(v.getSupplier().getTenantId(), vendorRequest.getRequestInfo()));
				v.getSupplier().setAuditDetails(v.getAuditDetails());
			}

			prepareAgreementDocument(v);

		}

		return vendorRepository.save(vendorRequest);

	}

	@Transactional
	public VendorRequest update(VendorRequest vendorRequest) {

		validate(vendorRequest);

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
				v.getSupplier().setTenantId(v.getTenantId());
				v.getSupplier().setSupplierNo(vendorSearchResult.getPagedData().get(0).getSupplier().getSupplierNo());
			}

			if (v.getAgreementDocument() != null && v.getAgreementDocument().getFileStoreId() != null) {

				v.getAgreementDocument().setTenantId(v.getTenantId());
				v.getAgreementDocument().setRefCode(v.getVendorNo());

			}

		}

		validate(vendorRequest);

		return vendorRepository.update(vendorRequest);

	}

	private void validate(VendorRequest vendorRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		SwmProcess p;
		Boundary boundary;
		findDuplicatesInUniqueFields(vendorRequest);

		for (Vendor vendor : vendorRequest.getVendors()) {

			if (vendor.getServicesOffered() != null)
				for (SwmProcess process : vendor.getServicesOffered()) {

					if (process != null && (process.getCode() == null || process.getCode().isEmpty()))
						throw new CustomException("ServicesOffered", "Code is missing in ServicesOffered");

					// Validate Swm Process
					if (process.getCode() != null) {

						responseJSONArray = mdmsRepository.getByCriteria(vendor.getTenantId(), Constants.MODULE_CODE,
								Constants.SWMPROCESS_MASTER_NAME, "code", process.getCode(),
								vendorRequest.getRequestInfo());

						if (responseJSONArray != null && responseJSONArray.size() > 0) {
							p = mapper.convertValue(responseJSONArray.get(0), SwmProcess.class);
							process.setTenantId(p.getTenantId());
							process.setName(p.getName());
						} else
							throw new CustomException("ServicesOffered",
									"Given ServicesOffered is invalid: " + process.getCode());

					}
				}

			if (vendor.getServicedLocations() != null)
				for (Boundary location : vendor.getServicedLocations()) {

					if (location != null && (location.getCode() == null || location.getCode().isEmpty()))
						throw new CustomException("Location",
								"The field Location Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

					// Validate Location
					if (location.getCode() != null) {

						boundary = boundaryRepository.fetchBoundaryByCode(location.getCode(), vendor.getTenantId());

						if (boundary != null)
							location.builder().id(String.valueOf(boundary.getId())).name(boundary.getName())
									.boundaryNum(String.valueOf(boundary.getBoundaryNum())).code(boundary.getCode())
									.build();
						else
							throw new CustomException("Location", "Given Location is Invalid: " + location.getCode());

					}
				}

			validateUniqueFields(vendor);

		}
	}

	private void findDuplicatesInUniqueFields(VendorRequest vendorRequest) {

		Map<String, String> regNumberMap = new HashMap<>();
		Map<String, String> gstMap = new HashMap<>();

		for (Vendor vendor : vendorRequest.getVendors()) {

			if (vendor.getRegistrationNo() != null) {
				if (regNumberMap.get(vendor.getRegistrationNo()) != null)
					throw new CustomException("registrationNo",
							"Duplicate registration numbers in given vendors : " + vendor.getRegistrationNo());

				regNumberMap.put(vendor.getRegistrationNo(), vendor.getRegistrationNo());
			}

			if (vendor.getSupplier() != null) {

				if (vendor.getSupplier().getGst() != null && !vendor.getSupplier().getGst().isEmpty()) {

					if (gstMap.get(vendor.getSupplier().getGst()) != null)
						throw new CustomException("gst",
								"Duplicate gst's in given vendors : " + vendor.getSupplier().getGst());

					gstMap.put(vendor.getSupplier().getGst(), vendor.getSupplier().getGst());
				}

			}

		}

	}

	private void validateUniqueFields(Vendor vendor) {

		if (vendor.getRegistrationNo() != null) {
			if (!vendorRepository.uniqueCheck(vendor.getTenantId(), "registrationNo", vendor.getRegistrationNo(),
					"vendorNo", vendor.getVendorNo())) {

				throw new CustomException("registrationNo",
						"The field registrationNo must be unique in the system The  value " + vendor.getRegistrationNo()
								+ " for the field registrationNo already exists in the system. Please provide different value ");

			}
		}

		if (vendor.getSupplier() != null) {

			if (vendor.getSupplier().getGst() != null && !vendor.getSupplier().getGst().isEmpty()) {

				if (!supplierRepository.uniqueCheck(vendor.getTenantId(), "gst", vendor.getSupplier().getGst(),
						"supplierNo", vendor.getSupplier().getSupplierNo())) {

					throw new CustomException("gst", "The field gst must be unique in the system The  value "
							+ vendor.getSupplier().getGst()
							+ " for the field gst already exists in the system. Please provide different value ");

				}
			}

		}

	}

	private void prepareAgreementDocument(Vendor v) {

		if (v.getAgreementDocument() != null && v.getAgreementDocument().getFileStoreId() != null) {
			v.getAgreementDocument().setId(UUID.randomUUID().toString().replace("-", ""));
			v.getAgreementDocument().setTenantId(v.getTenantId());
			v.getAgreementDocument().setRefCode(v.getVendorNo());
			v.getAgreementDocument().setAuditDetails(v.getAuditDetails());
		} else {

			v.setAgreementDocument(new Document());
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

	private String generateSupplierNumber(String tenantId, RequestInfo requestInfo) {

		String supplierNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForSupplierNumPath);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					supplierNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return supplierNumber;
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