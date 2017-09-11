package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.tl.commons.web.contract.FeeMatrix;
import org.egov.tl.commons.web.contract.FeeMatrixDetail;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.FeeMatrixResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.FeeMatrixValidator;
import org.egov.tradelicense.persistence.repository.FeeMatrixRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FeeMatrixService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class FeeMatrixServiceImpl implements FeeMatrixService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	FeeMatrixRepository feeMatrixRepository;

	@Autowired
	FeeMatrixValidator feeMatrixValidator;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private Producer Producer;

	@Override
	@Transactional
	public FeeMatrixResponse createFeeMatrixMaster(FeeMatrixRequest feeMatrixRequest) {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		feeMatrixValidator.validateFeeMatrixRequest(feeMatrixRequest, Boolean.TRUE);
		Producer.send(propertiesManager.getCreateFeeMatrixValidated(), feeMatrixRequest);
		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		feeMatrixResponse.setFeeMatrices(feeMatrixRequest.getFeeMatrices());
		feeMatrixResponse.setResponseInfo(responseInfo);

		return feeMatrixResponse;
	}

	public void persistNewFeeMatrix(FeeMatrixRequest feeMatrixRequest) {

		Long feeMatrixId;

		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			feeMatrixId = feeMatrixRepository.createFeeMatrix(feeMatrix);
			feeMatrix.setId(feeMatrixId);

			for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {

				feeMatrixDetail.setFeeMatrixId(feeMatrixId);
				feeMatrixRepository.createFeeMatrixDetails(feeMatrixDetail);
			}

		}

	}

	public FeeMatrixResponse updateFeeMatrixMaster(FeeMatrixRequest feeMatrixRequest) {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		feeMatrixValidator.validateFeeMatrixRequest(feeMatrixRequest, Boolean.FALSE);
		Producer.send(propertiesManager.getUpdateFeeMatrixValidated(), feeMatrixRequest);
		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		feeMatrixResponse.setFeeMatrices(feeMatrixRequest.getFeeMatrices());
		feeMatrixResponse.setResponseInfo(responseInfo);

		return feeMatrixResponse;
	}

	public void persistUpdatedFeeMatrix(FeeMatrixRequest feeMatrixRequest) {

		Long feeMatrixId;

		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			feeMatrixId = feeMatrix.getId();
			feeMatrix = feeMatrixRepository.updateFeeMatrix(feeMatrix);

			for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {

				feeMatrixDetail.setFeeMatrixId(feeMatrixId);
				feeMatrixDetail = feeMatrixRepository.updateFeeMatrixDetail(feeMatrixDetail);
			}

		}

	}

	public FeeMatrixResponse getFeeMatrixMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			Integer categoryId, Integer subCategoryId, String financialYear, String applicationType,
			String businessNature, Integer pageSize, Integer offSet) {

		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		try {
			List<FeeMatrix> feeMatrices = feeMatrixRepository.searchFeeMatrix(tenantId, ids, categoryId, subCategoryId,
					financialYear, applicationType, businessNature, pageSize, offSet);
			for (int i = 0; i < feeMatrices.size(); i++) {
				FeeMatrix feeMatrix = feeMatrices.get(i);
				List<FeeMatrixDetail> feeMatrixDetails = feeMatrixValidator
						.getFeeMatrixDetailsByFeeMatrixId(feeMatrix.getId());
				feeMatrix.setFeeMatrixDetails(feeMatrixDetails);
			}
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			feeMatrixResponse.setFeeMatrices(feeMatrices);
			feeMatrixResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return feeMatrixResponse;
	}
}