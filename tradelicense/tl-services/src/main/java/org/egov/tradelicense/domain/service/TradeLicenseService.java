package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.requests.CategoryResponse;
import org.egov.tl.commons.web.requests.DocumentTypeResponse;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.web.contract.TradeLicenseContract;
import org.egov.tradelicense.web.repository.BoundaryContractRepository;
import org.egov.tradelicense.web.repository.CategoryContractRepository;
import org.egov.tradelicense.web.repository.DocumentTypeContractRepository;
import org.egov.tradelicense.web.requests.BoundaryResponse;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
@Transactional(readOnly = true)
public class TradeLicenseService {

	@Autowired
	TradeLicenseRepository tradeLicenseRepository;

	@Autowired
	BoundaryContractRepository boundaryContractRepository;

	@Autowired
	CategoryContractRepository categoryContractRepository;

	@Autowired
	DocumentTypeContractRepository documentTypeContractRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<TradeLicense> tradeLicenses, BindingResult errors) {

		try {
			Assert.notNull(tradeLicenses, "tradeLicenses to create must not be null");
			for (TradeLicense tradeLicense : tradeLicenses) {
				validator.validate(tradeLicense, errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}

		return errors;

	}

	public List<TradeLicense> validateRelated(List<TradeLicense> tradeLicenses, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		for (TradeLicense tradeLicense : tradeLicenses) {

			if (tradeLicense.getIsLegacy()) {
				// check unique constraint
				tradeLicenseRepository.validateUniqueLicenseNumber(tradeLicense);
			} else {

			}

			// locality validation
			if (tradeLicense.getLocalityId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByLocalityId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidInputException("Invalid trade locality ");
				}

			}

			// revenue ward validation
			if (tradeLicense.getRevenueWardId() != null) {
				BoundaryResponse boundaryResponse = boundaryContractRepository.findByRevenueWardId(tradeLicense,
						requestInfoWrapper);

				if (boundaryResponse == null || boundaryResponse.getBoundarys() == null
						|| boundaryResponse.getBoundarys().size() == 0) {
					throw new InvalidInputException("Invalid location ward ");
				}

			}

			// category validation
			if (tradeLicense.getCategoryId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findByCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidInputException("Invalid category type ");
				}
			}

			// subCategory validation
			if (tradeLicense.getSubCategoryId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findBySubCategoryId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {
					throw new InvalidInputException("Invalid sub category type ");
				}
			}

			// supporting documents validation
			if (tradeLicense.getSupportDocuments() != null) {
				for (SupportDocument supportDocument : tradeLicense.getSupportDocuments()) {

					DocumentTypeResponse documentTypeResponse = documentTypeContractRepository.findById(tradeLicense,
							supportDocument, requestInfoWrapper);

					if (documentTypeResponse == null || documentTypeResponse.getDocumentTypes() == null
							|| documentTypeResponse.getDocumentTypes().size() == 0) {
						throw new InvalidInputException("Invalid document type ");
					}
				}

			}

			// uom validation
			if (tradeLicense.getUomId() != null) {
				CategoryResponse categoryResponse = categoryContractRepository.findBySubCategoryUomId(tradeLicense,
						requestInfoWrapper);

				if (categoryResponse == null || categoryResponse.getCategories() == null
						|| categoryResponse.getCategories().size() == 0) {

					throw new InvalidInputException("Invalid Uom");
				} else {

					for (Category category : categoryResponse.getCategories()) {

						if (category.getDetails() == null && category.getDetails().size() == 0) {
							throw new InvalidInputException("Invalid Uom");
						} else {
							Boolean isExists = false;
							for (CategoryDetail categoryDetail : category.getDetails()) {
								if (categoryDetail.getUomId() == tradeLicense.getUomId()) {
									isExists = true;
								}
							}
							if (!isExists) {
								throw new InvalidInputException("Invalid Uom");
							}
						}
					}
				}

			}

		}

		return tradeLicenses;
	}

	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		// external end point validations
		validateRelated(tradeLicenses, requestInfo);

		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			license.setId(tradeLicenseRepository.getNextSequence());
			if (license.getSupportDocuments() != null && license.getSupportDocuments().size() > 0) {
				for (SupportDocument supportDocument : license.getSupportDocuments()) {
					supportDocument.setLicenseId(license.getId());
					supportDocument.setId(tradeLicenseRepository.getSupportDocumentNextSequence());
				}
			}
			if (license.getFeeDetails() != null && license.getFeeDetails().size() > 0) {
				for (LicenseFeeDetail feeDetail : license.getFeeDetails()) {
					feeDetail.setLicenseId(license.getId());
					feeDetail.setId(tradeLicenseRepository.getFeeDetailNextSequence());
				}
			}
		}

		return tradeLicenses;
	}

	public void addToQue(TradeLicenseRequest request) {
		tradeLicenseRepository.add(request);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {
		return tradeLicenseRepository.save(tradeLicense);
	}

	public TradeLicenseResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, String tradeLicenseId, String applicationNumber,
			String licenseNumber, String mobileNumber, String aadhaarNumber, String emailId, String propertyAssesmentNo,
			Integer revenueWard, Integer locality, String ownerName, String tradeTitle, String tradeType,
			Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		List<TradeLicense> licenses = tradeLicenseRepository.search(tenantId, pageSize, pageNumber, sort, active,
				tradeLicenseId, applicationNumber, licenseNumber, mobileNumber, aadhaarNumber, emailId,
				propertyAssesmentNo, revenueWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);

		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<TradeLicenseContract>();

		ModelMapper model = new ModelMapper();

		for (TradeLicense license : licenses) {
			TradeLicenseContract tradeContract = new TradeLicenseContract();
			model.map(license, tradeContract);
			tradeLicenseContracts.add(tradeContract);
		}
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		TradeLicenseResponse.setResponseInfo(createResponseInfoFromRequestInfo(requestInfo, true));
		TradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return TradeLicenseResponse;
	}

	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {
		ResponseInfo responseInfo = new ResponseInfo();
		String apiId = requestInfo.getApiId();
		responseInfo.setApiId(apiId);
		String ver = requestInfo.getVer();
		responseInfo.setVer(ver);
		String ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs().toString();
		responseInfo.setTs(ts);
		String resMsgId = "uief87324";
		responseInfo.setResMsgId(resMsgId);
		String msgId = requestInfo.getMsgId();
		responseInfo.setMsgId(msgId);
		String responseStatus = (success.booleanValue()) ? "SUCCESSFUL" : "FAILED";
		responseInfo.setStatus(responseStatus);
		return responseInfo;
	}

}